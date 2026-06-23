package com.studyflow.reservation.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 座位状态推送处理器
 *
 * 连接方式：
 * ws://localhost:8082/api/ws/seats/{roomId}?date=20250605&periodId=1
 */
@Component
public class SeatWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = session.getUri().getPath();
        String roomId = extractRoomId(path);
        String sessionKey = roomId + "_" + session.getId();
        sessions.put(sessionKey, session);
        System.out.println("WebSocket 连接建立: " + sessionKey);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 目前只做推送，不处理客户端消息
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String path = session.getUri().getPath();
        String roomId = extractRoomId(path);
        String sessionKey = roomId + "_" + session.getId();
        sessions.remove(sessionKey);
        System.out.println("WebSocket 连接关闭: " + sessionKey);
    }

    /**
     * 向指定自习室的所有在线用户广播消息
     */
    public void broadcastToRoom(Long roomId, String message) {
        String prefix = roomId + "_";
        TextMessage textMessage = new TextMessage(message);
        sessions.forEach((key, session) -> {
            if (key.startsWith(prefix) && session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    System.err.println("WebSocket 推送失败: " + key);
                }
            }
        });
    }

    private String extractRoomId(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}
