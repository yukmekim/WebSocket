package org.websocket.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocket.chat.entity.Chat;
import org.websocket.chat.service.ChatService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/chat")
public class ChatApi {

    private final ChatService chatService;

    public ChatApi(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/createChat")
    public Mono<Map<String,String>> createChat() {
        return Mono.just(Map.of("status", "200"));
    }

}
