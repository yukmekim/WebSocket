package org.chat.service;

import org.chat.entity.Chat;
import org.chat.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ChatServiceTest {

    @MockBean
    private ChatRepository chatRepository;

    private ChatService chatService;

    @BeforeEach
    public void setChatService() {chatService = new ChatService(chatRepository); }

    @Test
    void getUserById() {
        Chat member = new Chat("sixman", "TENANT_000000000001", "6666","육장훈");
        Mockito.when(chatService.findChatRoomByMemberId("sixman")).thenReturn(Optional.of(member));

        // When
        Optional<Chat> result = chatService.findChatRoomByMemberId("sixman");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo("채팅1");
    }

}