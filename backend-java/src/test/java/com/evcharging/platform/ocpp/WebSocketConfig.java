package com.evcharging.platform.ocpp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OcppWebSocketHandler ocppWebSocketHandler;

    public WebSocketConfig(OcppWebSocketHandler ocppWebSocketHandler) {
        this.ocppWebSocketHandler = ocppWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // A töltőoszlopok erre az URL-re csatlakoznak, azonosítóval a path-ban
        registry.addHandler(ocppWebSocketHandler, "/ocpp/{chargePointId}")
                .setAllowedOrigins("*");
    }
}