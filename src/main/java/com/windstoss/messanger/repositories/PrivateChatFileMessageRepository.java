package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateChatFileMessageRepository extends JpaRepository<PrivateChatFileMessage, UUID> {
    List<PrivateChatFileMessage> findByChatId(UUID chatId);
}
