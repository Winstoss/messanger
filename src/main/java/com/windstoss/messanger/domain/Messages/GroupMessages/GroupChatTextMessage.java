package com.windstoss.messanger.domain.Messages.GroupMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "group_text_message")
@PrimaryKeyJoinColumn(name = "signature_id")
public class GroupChatTextMessage extends GroupChatMessageSignature {

    private String content;
}
