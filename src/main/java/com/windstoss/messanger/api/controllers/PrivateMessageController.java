package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.ControllerMessageMapper;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
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
@RequestMapping("/chats")
@RestController
public class PrivateMessageController {

    private final ControllerMessageMapper controllerMessageMapper;

    private final PrivateMessageService privateMessageService;

    public PrivateMessageController(PrivateMessageService privateMessageService,
                                    ControllerMessageMapper controllerMessageMapper) {
        this.privateMessageService = privateMessageService;
        this.controllerMessageMapper = controllerMessageMapper;
    }

    @GetMapping("/private/{chatId}/messages/")
    public List<PrivateChatTextMessage> getAllTextMessages(@PathVariable("chatId") UUID chatId,
                                                           UsernamePasswordAuthenticationToken principal) {
        return privateMessageService.getAllTextMessages((User) principal.getPrincipal(), chatId);

    }

    @PostMapping("/private/{chatId}/messages/")
    public PrivateChatTextMessage sendPrivateChatTextMessage(@RequestHeader("credentials") String credentials,
                                                             @PathVariable("chatId") UUID chatId,
                                                             @RequestBody SendTextMessageDto sendMessageDto
    ) {
        return privateMessageService.sendPrivateTextMessage(credentials, chatId, sendMessageDto);
    }


    @PatchMapping("/private/{chatId}/messages/{messageId}")
    public PrivateChatTextMessage editPrivateChatTextMessage(@RequestHeader("credentials") String credentials,
                                                             @PathVariable UUID chatId,
                                                             @PathVariable UUID messageId,
                                                             @RequestBody EditTextMessageDto data) {
        return privateMessageService.editPrivateTextMessage(credentials, chatId, messageId, data);
    }

    @DeleteMapping("/private/{chatId}/messages/{messageId}")
    public void deletePrivateTextMessage(@RequestHeader("credentials") String credentials,
                                         @PathVariable UUID chatId,
                                         @PathVariable UUID messageId) {
        privateMessageService.deletePrivateChatTextMessage(credentials, chatId, messageId);
    }


    @PostMapping(value = "/private/{chatId}/messages-new/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> sendFileMessage(@RequestHeader("credentials") String credentials,
                                        @PathVariable UUID chatId,
                                        @RequestParam MultipartFile file,
                                        @RequestParam(required = false) String text ) throws IOException {
        final SendMessageDto data = controllerMessageMapper.map(text, file, credentials, chatId);
        return privateMessageService.sendMessageWithFile(data);
    }


}
