package com.windstoss.messanger.domain.Messages.GroupMessages;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
public class GroupChatFileMessage extends GCMessage {
    private String path;
}
