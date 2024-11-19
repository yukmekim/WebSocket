package org.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private String sender;
    private MessageType type;
    private String content;
    private String roomId;

    public enum MessageType {
        MESSAGE,  // 채팅 메시지
        JOIN,  // 채팅방 참여 메시지
        LEAVE  // 채팅방 나가기 메시지
    }
}
