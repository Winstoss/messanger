package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.PrivateChat.PrivateChatDto;
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

import java.util.Objects;
import java.util.UUID;

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

    public PrivateChat getPrivateChat(String credentials, UUID chatId) {

        User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(UserNotFoundException::new);

        return assertChatExists(user.getId(), chatId);
    }


    public PrivateChat createPrivateChat(String firstUserData, PrivateChatDto secondUserData) {

        String secondUser = CreatePrivateChatDtoMapper.dtoToUser(secondUserData);

        User user1 = userExists(firstUserData);
        User user2 = userExists(secondUser);

        privateChatRepository.findByUsersId(user1.getId(), user2.getId()).ifPresent(x -> {
            throw new ChatAlreadyExistsException();
        });

        return privateChatRepository.save(PrivateChat.builder()
                .firstUser(user1)
                .secondUser(user2)
                .build());

    }

    public void deletePrivateChat(String credentials, UUID chatId) {

        User user = userExists(credentials);

        PrivateChat chat = assertChatExists(user.getId(), chatId);

        privateChatRepository.delete(chat);
    }

    private User userExists(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private PrivateChat assertChatExists(UUID user, UUID chatId) {
        return privateChatRepository.findChatById(chatId, user).orElseThrow(ChatNotFoundException::new);
    }
}
