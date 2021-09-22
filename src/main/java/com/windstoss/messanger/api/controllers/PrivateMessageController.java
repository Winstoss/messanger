package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.MessageMapper;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.services.PrivateMessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RequestMapping("/chat")
@RestController
public class PrivateMessageController {

    private final MessageMapper messageMapper;

    private final PrivateMessageService privateMessageService;

    public PrivateMessageController(PrivateMessageService privateMessageService,
                                    MessageMapper messageMapper) {
        this.privateMessageService = privateMessageService;
        this.messageMapper = messageMapper;
    }

    @PostMapping("/private/{chatId}/messages/")
    public PrivateChatTextMessage sendPrivateChatTextMessage(@RequestHeader("credentials") String credentials,
                                                             @PathVariable("chatId") UUID chatId,
                                                             @RequestBody SendTextMessageDto sendMessageDto
    ) {
        return privateMessageService.sendPrivateTextMessage(credentials, chatId, sendMessageDto);
    }


    @GetMapping("/private/{chatId}/messages/")
    public List<PrivateChatTextMessage> getAllTextMessages(@RequestHeader("credentials") String credentials,
                                                           @PathVariable("chatId") UUID chatId) {
        return privateMessageService.getAllTextMessages(credentials, chatId);

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
                                        @RequestParam String text) throws IOException {
        final SendMessageDto data = messageMapper.map(text, file, credentials, chatId);
        return privateMessageService.sendFileMessage(credentials, chatId, data);
    }


}
