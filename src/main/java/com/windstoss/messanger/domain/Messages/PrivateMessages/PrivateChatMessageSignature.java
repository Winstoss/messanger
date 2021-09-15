package com.windstoss.messanger.domain.Messages.PrivateMessages;

import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Message;
import com.windstoss.messanger.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "private_message_signature")
public class PrivateChatMessageSignature extends Message {

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private PrivateChat chat;

}
