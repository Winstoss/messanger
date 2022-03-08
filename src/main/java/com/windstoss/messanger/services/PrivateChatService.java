package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.Chats.ChatRetrievalDto;
import com.windstoss.messanger.api.dto.Chats.PrivateChat.PrivateChatDto;
import com.windstoss.messanger.api.exception.exceptions.ChatAlreadyExistsException;
import com.windstoss.messanger.api.exception.exceptions.ChatNotFoundException;
import com.windstoss.messanger.api.exception.exceptions.UserNotFoundException;
import com.windstoss.messanger.api.mapper.CreatePrivateChatDtoMapper;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.PrivateChatRepository;
import com.windstoss.messanger.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrivateChatService {

    private final UserRepository userRepository;

    private final PrivateChatRepository privateChatRepository;

    public PrivateChatService(
                       PrivateChatRepository privateChatRepository,
                       UserRepository userRepository) {
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public List<ChatRetrievalDto> getAllUsersChats(User user) {

        final List<PrivateChat> chats = privateChatRepository.findChatsByUserId(user.getId())
                .orElseGet(()-> (Collections.EMPTY_LIST));
        return chats.stream().map((it)->(ChatRetrievalDto.builder()
                .chatName((
                        it.getSecondUser().getId().equals(user.getId()))
                        ?(it.getFirstUser().getNickname())
                        :(it.getSecondUser().getNickname()))
                .chatId((
                        it.getSecondUser().getId().equals(user.getId()))
                        ?(it.getFirstUser().getId())
                        :(it.getSecondUser().getId()))
                .type("private")
                .chatImage((
                        it.getSecondUser().getId().equals(user.getId()))
                        ?(it.getFirstUser().getAvatarPath())
                        :(it.getSecondUser().getAvatarPath()))
                .build()))
                .collect(Collectors.toList());

    }

    public PrivateChat getPrivateChat(User requester, UUID secondUserID) {

        return privateChatRepository.findByUsersId(requester.getId(), secondUserID)
                .orElseThrow(ChatNotFoundException::new);
    }


    public PrivateChat createPrivateChat(User requester, UUID secondUserId) {

        User secondUser = userExists(secondUserId);

        privateChatRepository.findByUsersId(requester.getId(), secondUserId).ifPresent(x -> {
            throw new ChatAlreadyExistsException();
        });

        return privateChatRepository.save(PrivateChat.builder()
                .firstUser(requester)
                .secondUser(secondUser)
                .build());

    }

    public void deletePrivateChat(User requester, UUID secondUserId) {

        PrivateChat chat = assertChatExists(requester.getId(), userExists(secondUserId).getId());

        privateChatRepository.delete(chat);
    }

    public void deleteDeletedChat(User requester, UUID chatId) {

        if(!privateChatRepository.userPresentInChat(requester.getId(), chatId)){
            throw new ChatNotFoundException();
        };

        privateChatRepository.delete(privateChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new));
    }

    private User userExists(UUID userId){
        return userRepository.findUserById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private PrivateChat assertChatExists(UUID requester, UUID secondUserId) {
        return privateChatRepository.findByUsersId(requester, secondUserId).orElseThrow(ChatNotFoundException::new);
    }



}
