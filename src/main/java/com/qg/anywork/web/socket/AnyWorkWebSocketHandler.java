package com.qg.anywork.web.socket;

import com.google.gson.Gson;
import com.qg.anywork.model.po.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by ming on 18-8-18 上午8:44
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Component
@Slf4j
public class AnyWorkWebSocketHandler implements WebSocketHandler {

    private static Gson gson = new Gson();

    /**
     * 记录当前连接数
     */
    private static int onlineCount = 0;

    /**
     * 存放服务端与客户端的唯一标识符
     */
    private static ConcurrentHashMap<Integer, WebSocketSession> onlineMap = new ConcurrentHashMap<>();

    /**
     * 用户id
     */
    private int userId;

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        AnyWorkWebSocketHandler.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        AnyWorkWebSocketHandler.onlineCount--;
        if (onlineCount < 0) {
            onlineCount = 0;
        }
    }

    /**
     * 向在线用户推送公告
     *
     * @param message   消息
     * @param userIds   用户ID集合
     * @param publisher 发布人
     * @throws IOException ioException
     */
    public static void publishMessage(Message message, List<Integer> userIds, String publisher) throws IOException {
        Map<String, Object> map = new HashMap<>(6);
        // 2为公告推送
        map.put("type", 2);
        map.put("messageId", message.getMessageId());
        map.put("title", message.getTitle());
        map.put("content", message.getContent());
        map.put("publisher", publisher);
        map.put("status", 0);
        String response = gson.toJson(map);
        for (Integer userId : onlineMap.keySet()) {
            if (userIds.contains(userId)) {
                onlineMap.get(userId).sendMessage(new TextMessage(response));
            }
        }
    }

    /**
     * 向全部人发送在线人数信息
     *
     * @throws IOException ioException
     */
    public static void sendOnLineToAll() throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        // 1为在线人数
        map.put("type", 1);
        map.put("onlineCount", getOnlineCount());
        for (int userId : onlineMap.keySet()) {
            onlineMap.get(userId).sendMessage(new TextMessage(gson.toJson(map)));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Integer userId = (Integer) webSocketSession.getAttributes().get("userId");
        this.userId = userId;
        onlineMap.put(userId, webSocketSession);
        addOnlineCount();
        sendToUser(userId);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        log.info("收到信息:  {}", webSocketMessage.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        log.debug("连接发生错误，异常断开");
        onlineMap.remove(userId);
        subOnlineCount();
        log.info("连接断开，当前人数为 " + getOnlineCount());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        onlineMap.remove(userId);
        subOnlineCount();
        log.info("连接断开，当前人数为 " + getOnlineCount());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 向单个用户推送消息
     *
     * @param userId 用户ID
     * @throws IOException ioException
     */
    private void sendToUser(int userId) throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        // 1为在线人数
        map.put("type", 1);
        map.put("onlineCount", getOnlineCount());
        onlineMap.get(userId).sendMessage(new TextMessage(gson.toJson(map)));
    }

}
