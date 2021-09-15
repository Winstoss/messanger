package com.windstoss.messanger.services;


import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.PrivateTextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class MessageService {

    private final UserRepository userRepository;

    /*private final GroupChatFileMessageRepository groupChatFileMessageRepository;

    private final GroupChatTextMessageRepository groupChatTextMessageRepository;*/

    private final PrivateChatMessageSignatureRepository privateChatMessageSignatureRepository;

    private final PrivateChatRepository privateChatRepository;

    private final GroupChatRepository groupChatRepository;

    private final PrivateChatFileMessageRepository privateChatFileMessageRepository;

    private final PrivateChatTextMessageRepository privateChatTextMessageRepository;

    public MessageService(/*GroupChatFileMessageRepository groupChatFileMessageRepository,
                          GroupChatTextMessageRepository groupChatTextMessageRepository,*/
            PrivateChatMessageSignatureRepository privateChatMessageSignatureRepository,
            PrivateChatTextMessageRepository privateChatTextMessageRepository,
            PrivateChatFileMessageRepository privateChatFileMessageRepository,
            PrivateChatRepository privateChatRepository,
            GroupChatRepository groupChatRepository,
            UserRepository userRepository) {
        /*this.groupChatFileMessageRepository = Objects.requireNonNull(groupChatFileMessageRepository);
        this.groupChatTextMessageRepository = Objects.requireNonNull(groupChatTextMessageRepository)*/
        ;
        this.privateChatMessageSignatureRepository = Objects.requireNonNull(privateChatMessageSignatureRepository);
        this.privateChatTextMessageRepository = Objects.requireNonNull(privateChatTextMessageRepository);
        this.privateChatFileMessageRepository = Objects.requireNonNull(privateChatFileMessageRepository);
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public PrivateChatTextMessage sendPrivateTextMessage(String author, UUID chatId, SendTextMessageDto data) {
        User sender = userRepository.findUserByUsername(author)
                .orElseThrow(IllegalArgumentException::new);

        PrivateChat chat = privateChatRepository.findPrivateChatById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        return privateChatTextMessageRepository.save(PrivateTextMessageDtoMapper.map(sender, chat, data));
    }

    public List<PrivateChatTextMessage> getAllTextMessages(String user, UUID chatId) {
        userRepository.findUserByUsername(user).orElseThrow(IllegalArgumentException::new);

        return privateChatTextMessageRepository.findMessagesInChat(chatId);
    }

    public void deletePrivateChatTextMessage(String credentials, UUID chatId, UUID messageId) {
        userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);
        privateChatRepository.findPrivateChatById(chatId).orElseThrow(IllegalArgumentException::new);
        PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        privateChatTextMessageRepository.delete(message);
    }

    public PrivateChatTextMessage editPrivateTextMessage(String credentials,
                                                         UUID chatId,
                                                         UUID messageId,
                                                         EditTextMessageDto data) {
        userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);
        privateChatRepository.findPrivateChatById(chatId).orElseThrow(IllegalArgumentException::new);
        PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        if (StringUtils.isEmpty(data.getText())) {
            throw new IllegalArgumentException();
        }

        message.setContent(data.getText());
        return privateChatTextMessageRepository.save(message);
    }


}
