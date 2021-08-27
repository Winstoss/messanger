package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Chats.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {

    @Query(value = "SELECT * FROM private_chat WHERE firstUser_id = userId OR secondUser_id = userId")
    List<PrivateChat> findByChatUserId(UUID userId);

    @Query(value = "" +
            "SELECT * FROM private_chat " +
            "WHERE " +
            "    (firstUser_id = user1 AND secondUser_id = user2) OR" +
            "    (firstUser_id = user2 AND secondUser_id = user1)")
    Optional<PrivateChat> findByUsersId(UUID user1, UUID user2);
}
