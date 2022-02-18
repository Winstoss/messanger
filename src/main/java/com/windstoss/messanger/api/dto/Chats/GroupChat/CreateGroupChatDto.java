package com.windstoss.messanger.api.dto.Chats.GroupChat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class CreateGroupChatDto {

    private String title;

    private UUID creator;

    private String imagePath;

    private Set<UUID> admins;

    private Set<String> invitedUsers;

}
