package com.windstoss.messanger.domain.Messages.GroupMessages;

import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Message;
import com.windstoss.messanger.domain.User;
import lombok.AllArgsConstructor;
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
@Table(name = "group_message_signature")
public class GroupChatMessageSignature extends Message {

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private GroupChat chat;

}
