package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/chat")
@RestController
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/private/{chatId}/messages/")
    public PrivateChatTextMessage sendPrivateChatTextMessage(@RequestHeader("credentials") String credentials,
                                                             @PathVariable("chatId") UUID chatId,
                                                             @RequestBody SendTextMessageDto sendMessageDto
    ) {
        return messageService.sendPrivateTextMessage(credentials, chatId, sendMessageDto);
    }

    @GetMapping("/private/{chatId}/messages/")
    public List<PrivateChatTextMessage> getAllTextMessages(@RequestHeader("credentials") String credentials,
                                                           @PathVariable("chatId") UUID chatId)
    {
        return messageService.getAllTextMessages(credentials, chatId);

    }

    @PatchMapping("/private/{chatId}/messages/{messageId}")
    public PrivateChatTextMessage editPrivateChatTextMessage(@RequestHeader("credentials") String credentials,
                                                             @PathVariable UUID chatId,
                                                             @PathVariable UUID messageId,
                                                             @RequestBody EditTextMessageDto data
    ) {
        return messageService.editPrivateTextMessage(credentials, chatId, messageId, data);
    }

    @DeleteMapping("/private/{chatId}/messages/{messageId}")
    public void deletePrivateTextMessage(@RequestHeader("credentials") String credentials,
                                         @PathVariable UUID chatId,
                                         @PathVariable UUID messageId
    ) {
        messageService.deletePrivateChatTextMessage(credentials, chatId, messageId);
    }


}
