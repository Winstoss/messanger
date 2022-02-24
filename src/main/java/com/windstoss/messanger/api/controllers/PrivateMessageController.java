package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.MessageTypes;
import com.windstoss.messanger.api.exception.exceptions.InternalStorageException;
import com.windstoss.messanger.api.mapper.ControllerMessageMapper;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.PrivateMessageService;
import com.windstoss.messanger.utils.StringUtils;
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

    private final ControllerMessageMapper controllerMessageMapper;

    private final PrivateMessageService privateMessageService;

    public PrivateMessageController(PrivateMessageService privateMessageService,
                                    ControllerMessageMapper controllerMessageMapper) {
        this.privateMessageService = privateMessageService;
        this.controllerMessageMapper = controllerMessageMapper;
    }

    @GetMapping("/{chatId}/messages/")
    public List<MessageRetrievalDto> getAllMessages(@PathVariable("chatId") UUID chatId,
                                                           UsernamePasswordAuthenticationToken principal) {

        return privateMessageService.getAllTextMessages((User) principal.getPrincipal(), chatId);
    }

    @PostMapping(value = "/{chatId}/messages/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageRetrievalDto sendFileMessage(@PathVariable UUID chatId,
                                               @RequestHeader("type") MessageTypes type,
                                               @RequestParam(required = false) String text,
                                               @RequestParam(required = false) MultipartFile file,
                                               UsernamePasswordAuthenticationToken principal) {

        try{
            switch(type){
                case TEXT: return privateMessageService.sendPrivateTextMessage((User) principal.getPrincipal(), chatId, text);
                case FILE: return privateMessageService.sendPrivateFileMessage((User) principal.getPrincipal(), chatId, file);
                case DESCRIBED: return privateMessageService.sendPrivateDescribedFileMessage((User) principal.getPrincipal(), chatId, file, text);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }

        return MessageRetrievalDto.builder().text("your message is empty!").build();

    }


    @PatchMapping("{chatId}/messages/{messageId}")
    public MessageRetrievalDto editPrivateChatMessage(@PathVariable UUID chatId,
                                                      @PathVariable UUID messageId,
                                                      @RequestHeader("type") MessageTypes type,
                                                      @RequestParam(required = false) String text,
                                                      @RequestParam(required = false) MultipartFile file,
                                                      UsernamePasswordAuthenticationToken principal) {
        try{
            switch(type){
                case TEXT: return privateMessageService.editPrivateTextMessage((User) principal.getPrincipal(), chatId, messageId, text);
                case FILE: return privateMessageService.editPrivateFileMessage((User) principal.getPrincipal(), chatId, messageId, file);
                case DESCRIBED: return privateMessageService.editPrivateDescribedFileMessage((User) principal.getPrincipal(), chatId, messageId, file, text);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }

        return MessageRetrievalDto.builder().text("your message is empty!").build();

    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public boolean deletePrivateTextMessage(@PathVariable UUID chatId,
                                         @PathVariable UUID messageId,
                                         @RequestHeader("type") MessageTypes type,
                                         UsernamePasswordAuthenticationToken principal) {

        try{
            switch(type){
                case TEXT: return privateMessageService.deleteTextMessage((User) principal.getPrincipal(), chatId, messageId);
                case FILE: return privateMessageService.deleteFileMessage((User) principal.getPrincipal(), chatId, messageId);
                case DESCRIBED: return privateMessageService.deleteDescribedFileMessage((User) principal.getPrincipal(), chatId, messageId);
            }
        } catch (IOException e){
            throw new InternalStorageException();
        }
        return false;
    }





}
