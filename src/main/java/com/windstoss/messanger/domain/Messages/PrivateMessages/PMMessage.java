package com.windstoss.messanger.domain.Messages.PrivateMessages;

import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "private_message")
public class PMMessage extends Message {

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private PrivateChat chat;
}
