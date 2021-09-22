package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.GroupChatTextMessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.GroupChatTextMessageRetrievalDtoMapper;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupMessageService {

    private final UserRepository userRepository;

    private final GroupChatRepository groupChatRepository;

    private final GroupChatMessageSignatureRepository groupChatMessageSignatureRepository;

    private final GroupChatTextMessageRepository groupChatTextMessageRepository;

    private final GroupChatFileMessageRepository groupChatFileMessageRepository;

    public GroupMessageService(UserRepository userRepository,
                               GroupChatRepository groupChatRepository,
                               GroupChatMessageSignatureRepository groupChatMessageSignatureRepository,
                               GroupChatTextMessageRepository groupChatTextMessageRepository,
                               GroupChatFileMessageRepository groupChatFileMessageRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.groupChatMessageSignatureRepository = Objects.requireNonNull(groupChatMessageSignatureRepository);
        this.groupChatTextMessageRepository = Objects.requireNonNull(groupChatTextMessageRepository);
        this.groupChatFileMessageRepository = Objects.requireNonNull(groupChatFileMessageRepository);
    }


    public GroupChatTextMessage sendGroupTextMessage(String credentials, UUID chatId, SendTextMessageDto sendMessageDto) {
        final User sender = userRepository.findUserByUsername(credentials)
                .orElseThrow(IllegalArgumentException::new);

        final GroupChat chat = groupChatRepository.findById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        if (!chat.getUsers().contains(sender)){
            throw new IllegalArgumentException();
        }

        return groupChatTextMessageRepository.save(TextMessageDtoMapper.map(sender, chat, sendMessageDto));
    }

    public List<GroupChatTextMessageRetrievalDto> getAllTextMessages(String credentials, UUID chatId) {


        if(!groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new)
                .getUsers().contains(userRepository.findUserByUsername(credentials)
                        .orElseThrow(IllegalArgumentException::new))){
            throw new IllegalArgumentException();
        }

        return groupChatTextMessageRepository.findMessagesInChat(chatId).stream()
                .filter(Objects::nonNull)
                .map(GroupChatTextMessageRetrievalDtoMapper::map)
                .collect(Collectors.toList());
    }

    public GroupChatTextMessageRetrievalDto editGroupTextMessage(String credentials,
                                                     UUID chatId,
                                                     UUID messageId,
                                                     EditTextMessageDto data) {

        final User user = userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);

        if(!groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new).getUsers().contains(user)){
            throw new IllegalArgumentException();
        };

        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        if (StringUtils.isEmpty(data.getText())) {
            throw new IllegalArgumentException();
        }

        if (user != message.getAuthor() || !groupChatRepository.isAdminInGroupChat(chatId, user.getId())) {
            throw new IllegalArgumentException();
        }

        message.setContent(data.getText());
        return GroupChatTextMessageRetrievalDtoMapper.map(groupChatTextMessageRepository.save(message));
    }

    public void deleteGroupChatTextMessage(String credentials, UUID chatId, UUID messageId) {

        final User user = userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);

        if (!groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new).getUsers().contains(user)){
            throw new IllegalArgumentException();
        };
        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        if (user != message.getAuthor() || !groupChatRepository.isAdminInGroupChat(chatId, user.getId())) {
            throw new IllegalArgumentException();
        }

        groupChatTextMessageRepository.delete(message);
    }

}
