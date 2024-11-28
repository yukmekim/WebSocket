package org.websocket.chat.service;

import org.websocket.chat.entity.Chat;
import org.websocket.chat.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Optional<Chat> findChatRoomByChatId(Long chatId) {
        return chatRepository.findChatRoomByChatId(chatId);
    }
}
