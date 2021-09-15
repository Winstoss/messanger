package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatMessageSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateChatMessageSignatureRepository extends JpaRepository<PrivateChatMessageSignature, UUID> {

    Optional<PrivateChatMessageSignature> findById(UUID uuid);
}
