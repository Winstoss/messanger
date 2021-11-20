package com.windstoss.messanger.api.controllers;


import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.GroupChatTextMessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.ControllerMessageMapper;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.services.GroupMessageService;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/chat/group")
@RestController
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    private final ControllerMessageMapper controllerMessageMapper;

    public GroupMessageController(GroupMessageService groupMessageService,
                                  ControllerMessageMapper controllerMessageMapper) {
        this.groupMessageService = Objects.requireNonNull(groupMessageService);
        this.controllerMessageMapper = Objects.requireNonNull(controllerMessageMapper);
    }

    @PostMapping("/{chatId}/messages/")
    public GroupChatTextMessage sendGroupChatTextMessage(@RequestHeader("credentials") String credentials,
                                                         @PathVariable("chatId") UUID chatId,
                                                         @RequestBody SendTextMessageDto sendMessageDto
    ) {
        return groupMessageService.sendGroupTextMessage(credentials, chatId, sendMessageDto);
    }

    @PostMapping(value = "/{chatId}/messages-new/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> sendFileMessage(@RequestHeader("credentials") String credentials,
                                        @PathVariable UUID chatId,
                                        @RequestParam MultipartFile file,
                                        @RequestParam(required = false) String text ) throws IOException {
        final SendMessageDto data = controllerMessageMapper.map(text, file, credentials, chatId);
        return groupMessageService.sendMessageWithFile(data);
    }

    @Transactional
    @GetMapping("/{chatId}/messages/")
    public List<GroupChatTextMessageRetrievalDto> getAllTextMessages(@RequestHeader("credentials") String credentials,
                                                                     @PathVariable("chatId") UUID chatId) {
        return groupMessageService.getAllTextMessages(credentials, chatId);

    }


    @PatchMapping("/{chatId}/messages/{messageId}")
    public GroupChatTextMessageRetrievalDto editGroupChatTextMessage(@RequestHeader("credentials") String credentials,
                                                                     @PathVariable UUID chatId,
                                                                     @PathVariable UUID messageId,
                                                                     @RequestBody EditTextMessageDto data
    ) {
        return groupMessageService.editGroupTextMessage(credentials, chatId, messageId, data);
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public void deleteGroupTextMessage(@RequestHeader("credentials") String credentials,
                                       @PathVariable UUID chatId,
                                       @PathVariable UUID messageId
    ) {
        groupMessageService.deleteGroupChatTextMessage(credentials, chatId, messageId);
    }


}
