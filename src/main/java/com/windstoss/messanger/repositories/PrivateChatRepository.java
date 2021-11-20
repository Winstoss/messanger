package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {

    Optional<PrivateChat> findPrivateChatById(UUID id);

    //TODO : rework native to jpql

    @Query(value = "SELECT * FROM private_chat WHERE first_user_id=:userId OR second_user_id=:userId", nativeQuery = true)
    List<PrivateChat> findByChatUserId(UUID userId);

    @Query(value = "" +
            "SELECT * FROM private_chat " +
            "WHERE " +
            "    (first_user_id=:user1 AND second_user_id=:user2) OR" +
            "    (first_user_id=:user2 AND second_user_id=:user1)", nativeQuery = true)
    Optional<PrivateChat> findByUsersId(UUID user1, UUID user2);

    @Query(value = "SELECT p FROM PrivateChat p WHERE p.id = :chatId AND (p.firstUser.id = :userId OR p.secondUser.id = :userId)")
    Optional<PrivateChat> findChatById(UUID chatId, UUID userId);
}
