package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateChatTextMessageRepository extends JpaRepository<PrivateChatTextMessage, UUID> {
    List<PrivateChatTextMessage> findByChatId(UUID chatId);
}
