package org.chat.controller;

import org.chat.dto.Chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    // 클라이언트로부터 메시지가 들어올 때 호출됨
    @MessageMapping("/sendMessage")  // "/app/sendMessage" 경로로 메시지를 보내면 호출됨
    @SendTo("/topic/public")  // "/topic/public"을 구독한 모든 클라이언트에게 메시지 전송
    public Chat sendMessage(Chat message) {
        return message;  // 전달받은 메시지를 그대로 반환 (WebSocket을 통해 전달됨)
    }
}