package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.Chats.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, UUID> {

    @Query(value = "SELECT EXISTS (*) FROM group_chat_admin WHERE group_chat_id = groupChatId AND user_id = userId", nativeQuery=true)
    boolean isAdminInGroupChat(UUID groupChatId, UUID userId);
}
