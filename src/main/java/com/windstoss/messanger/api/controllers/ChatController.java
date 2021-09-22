package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.dto.PrivateChat.PrivateChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.services.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RequestMapping("/chat")
@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = Objects.requireNonNull(chatService);
    }

    @GetMapping("/private/{chatId}")
    public PrivateChat getPrivateChat(@RequestHeader("credentials") String credentials,
                                      @PathVariable("chatId") UUID chatId
    ) {
        return chatService.getPrivateChat(credentials, chatId);
    }


    @PostMapping("/private")
    public PrivateChat createPrivateChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody PrivateChatDto secondUser
    ) {
        return chatService.createPrivateChat(credentials, secondUser);
    }


    @DeleteMapping("/private/{chatId}")
    public void deletePrivateChat(@PathVariable UUID chatId) {
        chatService.deletePrivateChat(chatId);
    }

    @GetMapping("/group/{chatId}")
    public GroupChat getGroupChat(@RequestHeader("credentials") String credentials,
                                  @PathVariable("chatId") UUID chatId
    ) {
        return chatService.getGroupChat(credentials, chatId);
    }


    @PostMapping("/group")
    public GroupChat createGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody CreateGroupChatDto parameters
    ) {
        return chatService.createGroupChat(credentials, parameters);
    }


    @DeleteMapping("/group")
    public void deleteGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody DeleteGroupDto id
    ) {
        chatService.deleteGroupChat(id);
    }


    @PatchMapping("/group/{chatId}")
    public void editGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody EditGroupChatDto editData
    ) {
        chatService.editGroupChat(credentials, chatId, editData);
    }

}
