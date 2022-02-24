package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrivateChatDescribedFileMessageRepository extends JpaRepository<PrivateChatDescribedFileMessage, UUID> {

    List<PrivateChatDescribedFileMessage> findAllByChatId(UUID chatId);
}
