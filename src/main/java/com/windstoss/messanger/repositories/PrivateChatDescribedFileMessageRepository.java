package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateChatDescribedFileMessageRepository extends JpaRepository<PrivateChatDescribedFileMessage, UUID> {

    List<PrivateChatDescribedFileMessage> findAllByChatId(UUID chatId);
}
