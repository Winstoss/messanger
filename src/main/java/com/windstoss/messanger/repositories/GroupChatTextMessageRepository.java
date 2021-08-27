package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupChatTextMessageRepository extends JpaRepository<GroupChatTextMessage, UUID> {
    List<GroupChatTextMessage> findByChatId(UUID chatId);
}
