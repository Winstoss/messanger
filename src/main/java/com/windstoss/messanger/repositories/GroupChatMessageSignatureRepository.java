package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatMessageSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupChatMessageSignatureRepository extends JpaRepository<GroupChatMessageSignature, UUID> {

    Optional<GroupChatMessageSignature> findById(UUID uuid);
}
