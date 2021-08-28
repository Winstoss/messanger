package com.windstoss.messanger.domain.Messages.GroupMessages;

import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "group_message")
public class GCMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private GroupChat chat;

}
