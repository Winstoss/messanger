package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Chats.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, UUID> {

    //TODO: rework native to jpql

    @Query(value = "SELECT CASE WHEN EXISTS(" +
                            "SELECT * " +
                            "FROM group_chat_admin " +
                            "WHERE chat_id=:groupChatId AND admin_id=:userId" +
                    ")" +
                    "THEN CAST(1 AS BIT)" +
                    "ELSE CAST(0 AS BIT) END", nativeQuery=true)
    boolean isAdminInGroupChat(UUID groupChatId, UUID userId);

    @Query(value = "SELECT CASE WHEN EXISTS(" +
            "SELECT * " +
            "FROM group_chat_user " +
            "WHERE chat_id=:chatId AND user_id=:userId" +
            ")" +
            "THEN CAST(1 AS BIT)" +
            "ELSE CAST(0 AS BIT) END", nativeQuery=true)
    boolean isPresentInChat(UUID chatId, UUID userId);

    Optional<GroupChat> findById(UUID uuid);
}
