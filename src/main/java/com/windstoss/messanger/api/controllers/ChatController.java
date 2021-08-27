package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.EditGroupChatDto;
import com.windstoss.messanger.services.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/createPrivateChat")
    public void createPrivateChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody String secondUser
    ) {
        chatService.createPrivateChat(credentials, secondUser);
    }

    @PostMapping("/deletePrivateChat")
    public void deletePrivateChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody String secondUser
    ) {
        chatService.deletePrivateChat(credentials, secondUser);
    }

    @PostMapping("/createGroupChat")
    public void createGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody CreateGroupChatDto parameters
    ) {
        chatService.createGroupChat(credentials, parameters);
    }

    @PostMapping("/deleteGroupChat")
    public void deleteGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody UUID uuid
    ) {
        chatService.deleteGroupChat(uuid);
    }

    @PostMapping("/editGroupChat")
    public void editGroupChat(
            @RequestHeader("credentials") String credentials,
            @RequestBody EditGroupChatDto editData
    ){
        
    }

}
