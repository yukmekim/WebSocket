package org.chat.repository;

import org.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {
    // 사용자 ID로 채팅
    Optional<Chat> findChatRoomById(String memberId);
}
