package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupChatDescribedFileMessageRepository extends JpaRepository<GroupChatDescribedFileMessage, UUID> {
    List<GroupChatDescribedFileMessage> findAllByChatId(UUID chatId);
}
