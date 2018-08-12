package com.qg.anywork.web.socket;

import com.qg.anywork.config.GetHttpSessionConfigurator;
import com.qg.anywork.exception.user.UserNotLoginException;
import com.qg.anywork.model.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Create by ming on 18-8-11 上午9:13
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Component
@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
@Slf4j
public class OnlineWebSocket {

    /**
     * 记录当前连接数
     */
    private static int onlineCount = 0;
    /**
     * 存放服务端与客户端的唯一标识符
     */
    private static CopyOnWriteArraySet<OnlineWebSocket> webSockets = new CopyOnWriteArraySet<>();
    /**
     * 用户id和websocket的session绑定的路由表
     */
    private static Map<Integer, Session> routetab = new ConcurrentHashMap<>();
    /**
     * 与客户端的会话连接
     */
    private Session session;
    /**
     * 用户id
     */
    private int userId;

    /**
     * 浏览器Session
     */
    private HttpSession httpSession;

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        OnlineWebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        OnlineWebSocket.onlineCount--;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        webSockets.add(this);
        addOnlineCount();
        // 获取当前用户的httpsession
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        try {
            this.userId = ((User) httpSession.getAttribute("user")).getUserId();
        } catch (Exception e) {
            log.warn("用户连接WebSocket发生错误 ：", e);
            throw new UserNotLoginException("用户登录发生错误！");
        }
        // 绑定用户id与session会话
        routetab.put(userId, session);
        System.out.println("有新链接加入，当前人数为 " + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        subOnlineCount();
        routetab.remove(userId);
        System.out.println("连接断开，当前人数为 " + getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket 发生错误：");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message) {

    }

    public HttpSession getHttpSession() {
        return this.httpSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OnlineWebSocket webSocket = (OnlineWebSocket) o;

        if (userId != webSocket.userId) {
            return false;
        }
        if (session != null ? !session.equals(webSocket.session) : webSocket.session != null) {
            return false;
        }
        return httpSession != null ? httpSession.equals(webSocket.httpSession) : webSocket.httpSession == null;
    }

    @Override
    public int hashCode() {
        int result = session != null ? session.hashCode() : 0;
        result = 31 * result + userId;
        result = 31 * result + (httpSession != null ? httpSession.hashCode() : 0);
        return result;
    }
}
