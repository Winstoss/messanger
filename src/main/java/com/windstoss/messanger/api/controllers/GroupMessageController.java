package com.windstoss.messanger.api.controllers;


import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.MessageTypes;
import com.windstoss.messanger.api.exception.exceptions.InternalStorageException;
import com.windstoss.messanger.api.mapper.ControllerMessageMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.GroupMessageService;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/chats/group")
@RestController
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    private final ControllerMessageMapper controllerMessageMapper;

    public GroupMessageController(GroupMessageService groupMessageService,
                                  ControllerMessageMapper controllerMessageMapper) {
        this.groupMessageService = Objects.requireNonNull(groupMessageService);
        this.controllerMessageMapper = Objects.requireNonNull(controllerMessageMapper);
    }

    @GetMapping("/{chatId}/messages/")
    public List<MessageRetrievalDto> getAllMessages(@PathVariable("chatId") UUID chatId,
                                                    UsernamePasswordAuthenticationToken principal) {

        return groupMessageService.getAllMessages((User) principal.getPrincipal(), chatId);
    }


    @PostMapping(value = "/{chatId}/messages/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageRetrievalDto sendFileMessage(@PathVariable UUID chatId,
                                               @RequestHeader("type") MessageTypes type,
                                               @RequestParam(required = false) String text,
                                               @RequestParam(required = false) MultipartFile file,
                                               UsernamePasswordAuthenticationToken principal){
        try{
            switch(type){
                case TEXT: return groupMessageService.sendTextMessage((User) principal.getPrincipal(), chatId, text);
                case FILE: return groupMessageService.sendFileMessage((User) principal.getPrincipal(), chatId, file);
                case DESCRIBED: return groupMessageService.sendDescribedFileMessage((User) principal.getPrincipal(), chatId, file, text);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }
        return MessageRetrievalDto.builder().text("your message is empty!").build();
    }




    @PatchMapping("/{chatId}/messages/{messageId}")
    public MessageRetrievalDto editGroupChatTextMessage(@PathVariable UUID chatId,
                                                        @PathVariable UUID messageId,
                                                        @RequestHeader("type") MessageTypes type,
                                                        @RequestParam(required = false) String text,
                                                        @RequestParam(required = false) MultipartFile file,
                                                        UsernamePasswordAuthenticationToken principal
    ) {
        try{
            switch(type){
                case TEXT: return groupMessageService.editTextMessage((User) principal.getPrincipal(), chatId, messageId, text);
                case FILE: return groupMessageService.editFileMessage((User) principal.getPrincipal(), chatId, messageId, file);
                case DESCRIBED: return groupMessageService.editDescribedFileMessage((User) principal.getPrincipal(), chatId, messageId, file, text);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }

        return MessageRetrievalDto.builder().text("your message is empty!").build();
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public boolean deleteGroupTextMessage(@PathVariable UUID chatId,
                                          @PathVariable UUID messageId,
                                          @RequestHeader("type") MessageTypes type,
                                          UsernamePasswordAuthenticationToken principal
    ) {
        try{
            switch(type){
                case TEXT: return groupMessageService.deleteTextMessage((User) principal.getPrincipal(), chatId, messageId);
                case FILE: return groupMessageService.deleteFileMessage((User) principal.getPrincipal(), chatId, messageId);
                case DESCRIBED: return groupMessageService.deleteDescribedFileMessage((User) principal.getPrincipal(), chatId, messageId);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }
        return false;
    }


}
