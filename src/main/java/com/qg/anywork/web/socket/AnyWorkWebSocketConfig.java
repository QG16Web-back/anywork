package com.qg.anywork.web.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Create by ming on 18-8-18 上午8:50
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Configuration
@EnableWebSocket
public class AnyWorkWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private AnyWorkWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(handler, "/ws").addInterceptors(new HandShake()).setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(handler, "/ws/websocket").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
    }
}
