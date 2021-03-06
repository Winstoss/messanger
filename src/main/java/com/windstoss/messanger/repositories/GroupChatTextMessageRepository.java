package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupChatTextMessageRepository extends JpaRepository<GroupChatTextMessage, UUID> {

    @Query(value =  "SELECT g FROM GroupChatTextMessage g WHERE g.chat.id = :chatId")
    List<GroupChatTextMessage> getAllMessagesInChat(UUID chatId);

}

