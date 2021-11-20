package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.dto.PrivateChat.PrivateChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.services.GroupChatService;
import com.windstoss.messanger.services.PrivateChatService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RequestMapping("/chat")
@RestController
public class ChatController {

    private final GroupChatService groupChatService;

    private final PrivateChatService privateChatService;

    public ChatController(GroupChatService groupChatService,
                          PrivateChatService privateChatService) {
        this.privateChatService = Objects.requireNonNull(privateChatService);
        this.groupChatService = Objects.requireNonNull(groupChatService);
    }

    @GetMapping("/private/{chatId}")
    public PrivateChat getPrivateChat(@RequestHeader("credentials") String credentials,
                                      @PathVariable("chatId") UUID chatId
    ) {
        return privateChatService.getPrivateChat(credentials, chatId);
    }


    @PostMapping("/private")
    public PrivateChat createPrivateChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody PrivateChatDto secondUser
    ) {
        return privateChatService.createPrivateChat(credentials, secondUser);
    }


    @DeleteMapping("/private/{chatId}")
    public void deletePrivateChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable UUID chatId) {
        privateChatService.deletePrivateChat(credentials, chatId);
    }

    @GetMapping("/group/{chatId}")
    public GroupChat getGroupChat(@RequestHeader("credentials") String credentials,
                                  @PathVariable("chatId") UUID chatId
    ) {
        return groupChatService.getGroupChat(credentials, chatId);
    }


    @PostMapping("/group")
    public GroupChat createGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody CreateGroupChatDto parameters
    ) {
        return groupChatService.createGroupChat(credentials, parameters);
    }


    @DeleteMapping("/group")
    public void deleteGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody DeleteGroupDto id
    ) {
        groupChatService.deleteGroupChat(id);
    }


    @PatchMapping("/group/{chatId}")
    public void editGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody EditGroupChatDto editData
    ) {
        groupChatService.editGroupChat(credentials, chatId, editData);
    }

}
