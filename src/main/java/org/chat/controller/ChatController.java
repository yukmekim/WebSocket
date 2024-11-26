package org.chat.controller;

import org.chat.dto.ChatDto;
import org.chat.entity.Chat;
import org.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.testng.mustache.Model;

import java.util.Optional;


@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chatList")
    public String chat(ModelMap model) {
        Optional<Chat> chat = chatService.findChatRoomByChatId(1L);

        model.addAttribute("chat", chat.orElse(null));
        return "chat/chatList";
    }

    // 클라이언트로부터 메시지가 들어올 때 호출됨
    @MessageMapping("/sendMessage")  // "/app/sendMessage" 경로로 메시지를 보내면 호출됨
    @SendTo("/topic/public")  // "/topic/public"을 구독한 모든 클라이언트에게 메시지 전송
    public ChatDto sendMessage(ChatDto message) {
        return message;  // 전달받은 메시지를 그대로 반환 (WebSocket을 통해 전달됨)
    }
}