package org.websocket.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.websocket.chat.dto.ChatDto;
import org.websocket.chat.entity.Chat;
import org.websocket.chat.service.ChatService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 채팅방 정보 리파지토리 선언
    private ChatService chatService;

    // 현재 연결된 세션 관리 (간단한 메모리 기반)
    private final Map<String, List<WebSocketSession>> activeSessions = new HashMap<>();
    private Map<String, String> roomSessions = new HashMap<>(); // roomId -> sender

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // WebSocket 연결이 성공적으로 열리면, 클라이언트로부터 'JOIN' 메시지가 오지 않았기 때문에,
        // 먼저 이 세션에서 'sender'와 'roomId' 값을 받아온 뒤, 처리합니다.
        String message = "{\"type\": \"INFO\", \"content\": \"Connected to chat\"}";

        session.sendMessage(new TextMessage(message));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 받은 메시지 처리
        String payload = message.getPayload();
        Map<String, Object> messageData = new ObjectMapper().readValue(payload, Map.class);

        // 메시지 타입 확인
        String type = (String) messageData.get("type");

        if ("JOIN".equals(type)) {
            // JOIN 메시지 처리
            String memberId = (String) messageData.get("memberId");  // 클라이언트에서 보낸 sender
            String roomId = (String) messageData.get("roomId");  // 클라이언트에서 보낸 roomId

            // 세션에 sender와 roomId를 저장
            session.getAttributes().put("memberId", memberId);
            session.getAttributes().put("roomId", roomId);

            // 세션을 activeSessions에 추가
            //activeSessions.put(sender, session);
            // 채팅방 세션에 사용자의 세션 추가
            activeSessions.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);
//            Chat chat = Chat.builder()
//                        .chatName(memberId)
//                        .build();
//
//            chatService.save(chat);

            // 채팅방에 입장한 것을 알리는 메시지 전송
            sendToAll(new ChatDto(memberId, ChatDto.MessageType.JOIN, memberId + " 님이 입장했습니다.", roomId));

            roomSessions.put(roomId, memberId); // 채팅방과 유저 매핑 저장
        } else {
            // 다른 타입의 메시지 처리 (예: CHAT)
            String sender = (String) session.getAttributes().get("sender");
            String roomId = (String) session.getAttributes().get("roomId");

            if ("MESSAGE".equals(type)) {
                // MESSAGE 처리 로직
                sendToAll(new ChatDto(sender, ChatDto.MessageType.MESSAGE, messageData.get("content").toString(), roomId));
            } else if ("LEAVE".equals(type)) {
                // LEAVE 처리 로직
                sendToAll(new ChatDto(sender, ChatDto.MessageType.LEAVE, messageData.get("content").toString(), roomId));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트가 연결을 종료했을 때 처리
        String roomId = (String) session.getAttributes().get("roomId");

        // 채팅방에서 해당 사용자의 세션 제거
        List<WebSocketSession> roomSessions = activeSessions.get(roomId);
        if (roomSessions != null) {
            roomSessions.remove(session);
        }
    }

    private void sendToAll(ChatDto message) throws IOException {
        // 모든 세션에 메시지를 전송
        ObjectMapper objectMapper = new ObjectMapper();
        String messageJson = objectMapper.writeValueAsString(message);  // Chat 객체를 JSON 문자열로 변환

        List<WebSocketSession> roomSessions = activeSessions.get(message.getRoomId());
        if (roomSessions != null) {
            for (WebSocketSession session : roomSessions) {
                session.sendMessage(new TextMessage(messageJson));
            }
        }
    }
}

