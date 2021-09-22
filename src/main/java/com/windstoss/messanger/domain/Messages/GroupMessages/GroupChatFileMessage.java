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
@Table(name = "group_file_message")
@PrimaryKeyJoinColumn(name = "signature_id")
public class GroupChatFileMessage extends GroupChatMessageSignature {

    @Column(name = "file_path")
    private String filePath;
}
