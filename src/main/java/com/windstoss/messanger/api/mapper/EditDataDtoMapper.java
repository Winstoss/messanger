package com.windstoss.messanger.api.mapper;

import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.utils.StringUtils;

import java.util.Set;

public class EditDataDtoMapper {

    public static GroupChat editDataDtoToGroupChat(EditGroupChatDto editData) {
        return GroupChat.builder()
                .imagePath(StringUtils.defaultIfEmpty(editData.getImagePath(), ""))
                .title(editData.getDescription())
                .build();
    }
}
