package com.windstoss.messanger.api.dto;

import lombok.Data;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Data
public class EditGroupChatDto {

    private UUID id;
    private String description;
    private Set<UUID> admins = Collections.emptySet();
    private Set<UUID> users = Collections.emptySet();
}
