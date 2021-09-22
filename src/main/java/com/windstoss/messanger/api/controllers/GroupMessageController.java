package com.windstoss.messanger.api.controllers;


import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.GroupChatTextMessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.services.GroupMessageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/chat/group")
@RestController
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    public GroupMessageController(GroupMessageService groupMessageService) {
        this.groupMessageService = Objects.requireNonNull(groupMessageService);
    }

    @PostMapping("/{chatId}/messages/")
    public GroupChatTextMessage sendGroupChatTextMessage(@RequestHeader("credentials") String credentials,
                                                         @PathVariable("chatId") UUID chatId,
                                                         @RequestBody SendTextMessageDto sendMessageDto
    ) {
        return groupMessageService.sendGroupTextMessage(credentials, chatId, sendMessageDto);
    }

    @Transactional
    @GetMapping("/{chatId}/messages/")
    public List<GroupChatTextMessageRetrievalDto> getAllTextMessages(@RequestHeader("credentials") String credentials,
                                                                     @PathVariable("chatId") UUID chatId) {
        return groupMessageService.getAllTextMessages(credentials, chatId);

    }


    @PatchMapping("/{chatId}/messages/{messageId}")
    public GroupChatTextMessageRetrievalDto editGroupChatTextMessage(@RequestHeader("credentials") String credentials,
                                                                     @PathVariable UUID chatId,
                                                                     @PathVariable UUID messageId,
                                                                     @RequestBody EditTextMessageDto data
    ) {
        return groupMessageService.editGroupTextMessage(credentials, chatId, messageId, data);
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public void deleteGroupTextMessage(@RequestHeader("credentials") String credentials,
                                       @PathVariable UUID chatId,
                                       @PathVariable UUID messageId
    ) {
        groupMessageService.deleteGroupChatTextMessage(credentials, chatId, messageId);
    }

}
