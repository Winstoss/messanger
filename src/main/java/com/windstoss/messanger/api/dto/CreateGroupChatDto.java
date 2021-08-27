package com.windstoss.messanger.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateGroupChatDto {

    private String description;

    private UUID creator;

    private Set<UUID> admins;

    private List<UUID> invitedUsers;

}
