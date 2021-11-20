package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupChatDescribedFileMessageRepository extends JpaRepository<GroupChatDescribedFileMessage, UUID> {
}
