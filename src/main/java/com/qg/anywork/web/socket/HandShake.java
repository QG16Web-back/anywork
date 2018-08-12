package com.qg.anywork.web.socket;

import com.qg.anywork.model.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Create by ming on 18-8-11 上午9:26
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
//@Component
@Slf4j
public class HandShake implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) serverHttpRequest;
            User user = (User) request.getSession().getAttribute("user");
            Integer userId = user.getUserId();
            map.put("user", userId);
        }
        log.info("握手之前: " + serverHttpRequest.getRemoteAddress().toString());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        log.info("握手之后: " + serverHttpRequest.getRemoteAddress().toString());
    }
}
