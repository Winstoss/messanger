package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatMessageSignature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupChatMessageSignatureRepository extends JpaRepository<GroupChatMessageSignature, UUID> {

    Optional<GroupChatMessageSignature> findById(UUID uuid);
}
