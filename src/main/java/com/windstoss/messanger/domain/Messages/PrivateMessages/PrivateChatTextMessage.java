package com.windstoss.messanger.domain.Messages.PrivateMessages;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
public class PrivateChatTextMessage extends PMMessage {

    private String text;
}
