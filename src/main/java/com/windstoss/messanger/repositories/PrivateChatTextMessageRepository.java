package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateChatTextMessageRepository extends JpaRepository<PrivateChatTextMessage, UUID> {

    @Query(value = "SELECT p FROM PrivateChatTextMessage p WHERE p.chat.id = :chatId")
    List<PrivateChatTextMessage> findMessagesInChat(UUID chatId);
}
