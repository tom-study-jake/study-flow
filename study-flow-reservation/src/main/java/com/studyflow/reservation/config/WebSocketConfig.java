package com.studyflow.reservation.config;

import com.studyflow.reservation.websocket.SeatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(seatWebSocketHandler(), "/api/ws/seats/{roomId}")
                .setAllowedOrigins("*");
    }

    @Bean
    public SeatWebSocketHandler seatWebSocketHandler() {
        return new SeatWebSocketHandler();
    }
}
