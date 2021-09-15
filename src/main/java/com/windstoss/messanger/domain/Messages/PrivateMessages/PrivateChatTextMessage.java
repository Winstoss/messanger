package com.windstoss.messanger.domain.Messages.PrivateMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "private_text_message")
@PrimaryKeyJoinColumn(name = "signature_id")
public class PrivateChatTextMessage extends PrivateChatMessageSignature {

    private String content;
}
