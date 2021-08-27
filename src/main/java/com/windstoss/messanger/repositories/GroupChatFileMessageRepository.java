package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupChatFileMessageRepository extends JpaRepository<GroupChatFileMessage, UUID> {
    List<GroupChatFileMessage> findByChatId(UUID chatId);
}
