package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.MessageTypes;
import com.windstoss.messanger.api.exception.exceptions.InternalStorageException;
import com.windstoss.messanger.api.mapper.ControllerMessageMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.PrivateMessageService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/chats/private")
@RestController
public class PrivateMessageController {

    private final PrivateMessageService privateMessageService;

    public PrivateMessageController(PrivateMessageService privateMessageService) {
        this.privateMessageService = privateMessageService;
    }


    @GetMapping("/{userId}/messages/")
    public List<MessageRetrievalDto> getAllMessages(@PathVariable("userId") UUID userId,
                                                    UsernamePasswordAuthenticationToken principal) {

        return privateMessageService.getAllPrivateMessages((User) principal.getPrincipal(), userId);
    }

    @PostMapping(value = "/{userId}/messages/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageRetrievalDto sendPrivateMessage(@PathVariable("userId") UUID userId,
                                                  @RequestHeader("type") MessageTypes type,
                                                  @RequestParam(required = false) String text,
                                                  @RequestParam(required = false) MultipartFile file,
                                                  UsernamePasswordAuthenticationToken principal) {

        try {
            switch (type) {
                case TEXT:
                    privateMessageService.sendPrivateTextMessage((User) principal.getPrincipal(), userId, text);
                    break;
                case FILE:
                    privateMessageService.sendPrivateFileMessage((User) principal.getPrincipal(), userId, file);
                    break;
                case DESCRIBED:
                    privateMessageService.sendPrivateDescribedFileMessage((User) principal.getPrincipal(), userId, file, text);
                    break;
            }
        } catch (IOException e) {
            throw new InternalStorageException();
        }

        return MessageRetrievalDto.builder().text("your message is empty!").build();

    }


    @PatchMapping("{userId}/messages/{messageId}")
    public MessageRetrievalDto editPrivateMessage(@PathVariable("userId") UUID userId,
                                                  @PathVariable("messageId") UUID messageId,
                                                  @RequestHeader("type") MessageTypes type,
                                                  @RequestParam(required = false) String text,
                                                  @RequestParam(required = false) MultipartFile file,
                                                  UsernamePasswordAuthenticationToken principal) {
        try {
            switch (type) {
                case TEXT:
                    privateMessageService.editPrivateTextMessage((User) principal.getPrincipal(), userId, messageId, text);
                    break;
                case FILE:
                    privateMessageService.editPrivateFileMessage((User) principal.getPrincipal(), userId, messageId, file);
                    break;
                case DESCRIBED:
                    privateMessageService.editPrivateDescribedFileMessage((User) principal.getPrincipal(), userId, messageId, file, text);
                    break;
            }
        } catch (IOException e) {
            throw new InternalStorageException();
        }

        return MessageRetrievalDto.builder().text("your message is empty!").build();

    }

    @DeleteMapping("/{userId}/messages/{messageId}")
    public boolean deletePrivateMessage(@PathVariable("userId") UUID userId,
                                        @PathVariable("messageId") UUID messageId,
                                        @RequestHeader("type") MessageTypes type,
                                        UsernamePasswordAuthenticationToken principal) {

        try {
            switch (type) {
                case TEXT:
                    privateMessageService.deleteTextMessage((User) principal.getPrincipal(), userId, messageId);
                    break;
                case FILE:
                    privateMessageService.deleteFileMessage((User) principal.getPrincipal(), userId, messageId);
                    break;
                case DESCRIBED:
                    privateMessageService.deleteDescribedFileMessage((User) principal.getPrincipal(), userId, messageId);
                    break;
            }
        } catch (IOException e) {
            throw new InternalStorageException();
        }
        return false;
    }


}
