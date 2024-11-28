package org.websocket.chat.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.websocket.chat.dto.ChatDto;
import org.websocket.chat.entity.Chat;
import org.websocket.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.websocket.member.entity.Member;
import org.websocket.member.service.MemberService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;

    public ChatController(ChatService chatService, MemberService memberService) {
        this.chatService = chatService;
        this.memberService = memberService;
    }

    @GetMapping("/chatList")
    public String chat(ModelMap model) {
//        PageRequest pageRequest = PageRequest.of(1, 10);

        Optional<Chat> chat = chatService.findChatRoomByChatId(1L);
        List<Member> memberList = memberService.findAll();

        model.addAttribute("chat", chat.orElse(null));
        model.addAttribute("memberList", memberList);
        return "chat/chatList";
    }

    // 클라이언트로부터 메시지가 들어올 때 호출됨
    @MessageMapping("/sendMessage")  // "/app/sendMessage" 경로로 메시지를 보내면 호출됨
    @SendTo("/topic/public")  // "/topic/public"을 구독한 모든 클라이언트에게 메시지 전송
    public ChatDto sendMessage(ChatDto message) {
        return message;  // 전달받은 메시지를 그대로 반환 (WebSocket을 통해 전달됨)
    }
}