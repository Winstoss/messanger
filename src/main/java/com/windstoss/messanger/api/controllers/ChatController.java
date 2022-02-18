package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Chats.ChatRetrievalDto;
import com.windstoss.messanger.api.dto.Chats.ChatsRetrievalDto;
import com.windstoss.messanger.api.dto.Chats.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.Chats.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.Chats.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.dto.Chats.GroupChat.ManageUserInGroupChatDto;
import com.windstoss.messanger.api.dto.Chats.PrivateChat.PrivateChatDto;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.GroupChatService;
import com.windstoss.messanger.services.PrivateChatService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final GroupChatService groupChatService;

    private final PrivateChatService privateChatService;

    public ChatController(GroupChatService groupChatService,
                          PrivateChatService privateChatService) {
        this.privateChatService = Objects.requireNonNull(privateChatService);
        this.groupChatService = Objects.requireNonNull(groupChatService);
    }

    @GetMapping
    public List<ChatRetrievalDto> getAllUsersChats(UsernamePasswordAuthenticationToken principal)
    {
        final User user = (User) principal.getPrincipal();
        List<ChatRetrievalDto> chatList = groupChatService.getAllUsersChats(user);
        chatList.addAll(privateChatService.getAllUsersChats(user));

        return chatList;

    }

    @GetMapping("/private/{chatId}")
    public PrivateChat getPrivateChat(@PathVariable("chatId") UUID chatId,
                                      UsernamePasswordAuthenticationToken principal

    ) {
        final User user = (User) principal.getPrincipal();
        return privateChatService.getPrivateChat(user.getUsername(), chatId);
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


    @PatchMapping(value = "/group/{chatId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void editGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String title
            )  {
        groupChatService.editGroupChat(credentials, chatId, EditGroupChatDto.builder()
                .image(image)
                .title(title)
                .build());
    }


    @PatchMapping("/group/{chatId}/add-admin")
    public void setAdminInGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody ManageUserInGroupChatDto editData
    ) {
        groupChatService.setAdminInGroupChat(credentials, chatId, editData);
    }


    @PatchMapping("/group/{chatId}/delete-admin")
    public void deleteAdminInGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody ManageUserInGroupChatDto editData
    ) {
        groupChatService.removeAdminInGroupChat(credentials, chatId, editData);
    }


    @PatchMapping("/group/{chatId}/user-add")
    public void addUserInGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody ManageUserInGroupChatDto editData
    ) {
        groupChatService.addUserInGroupChat(credentials, chatId, editData);
    }
    

    @PatchMapping("/group/{chatId}/user-delete")
    public void deleteUserInGroupChat(
            @RequestHeader("credentials") String credentials,
            @PathVariable("chatId") UUID chatId,
            @RequestBody ManageUserInGroupChatDto editData
    ) {
        groupChatService.deleteUserInGroupChat(credentials, chatId, editData);
    }

}
