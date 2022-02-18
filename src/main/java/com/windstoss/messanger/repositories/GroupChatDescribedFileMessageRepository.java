package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupChatDescribedFileMessageRepository extends JpaRepository<GroupChatDescribedFileMessage, UUID> {
    List<GroupChatDescribedFileMessage> findAllByChatId(UUID chatId);
}
