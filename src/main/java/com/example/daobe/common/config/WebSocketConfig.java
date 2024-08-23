package com.example.daobe.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * WebSocket Handshake 연결 엔드포인트의 URL -> ws:// 요청 보내야 힘
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/init")
                .setAllowedOrigins("*");
        //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 수신하는 요청 엔드포인트
        registry.enableSimpleBroker("/sub");

        // 메시지를 송신하는 엔드포인트
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
