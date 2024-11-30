package org.websocket.chat.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    private Long chatId;
    private String chatName;
    private String type;
    private Date createdAt;
}
