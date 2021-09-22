package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.dto.PrivateChat.PrivateChatDto;
import com.windstoss.messanger.api.mapper.CreatePrivateChatDtoMapper;
import com.windstoss.messanger.api.mapper.EditDataDtoMapper;
import com.windstoss.messanger.api.mapper.GroupChatDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.GroupChatRepository;
import com.windstoss.messanger.repositories.PrivateChatRepository;
import com.windstoss.messanger.repositories.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {
    private final UserRepository userRepository;

    private final GroupChatRepository groupChatRepository;

    private final PrivateChatRepository privateChatRepository;

    public ChatService(GroupChatRepository groupChatRepository,
                       PrivateChatRepository privateChatRepository,
                       UserRepository userRepository) {
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public PrivateChat getPrivateChat(String credentials, UUID chatId) {

        User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(IllegalArgumentException::new);

        return privateChatRepository.findPrivateChatById(chatId).orElseThrow(IllegalArgumentException::new);
    }


    public PrivateChat createPrivateChat(String firstUserData, PrivateChatDto secondUserData) {

        String secondUser = CreatePrivateChatDtoMapper.dtoToUser(secondUserData);

        User user1 = userRepository.findUserByUsername(firstUserData)
                .orElseThrow(IllegalArgumentException::new);
        User user2 = userRepository.findUserByUsername(secondUser)
                .orElseThrow(IllegalArgumentException::new);

        privateChatRepository.findByUsersId(user1.getId(), user2.getId()).ifPresent(x -> {
            throw new IllegalArgumentException();
        });

        return privateChatRepository.save(PrivateChat.builder()
                .firstUser(user1)
                .secondUser(user2)
                .build());

    }

    public void deletePrivateChat(UUID chatId) {

        PrivateChat chat = privateChatRepository.findPrivateChatById(chatId).orElseThrow(IllegalArgumentException::new);

        privateChatRepository.delete(chat);
    }

    public GroupChat getGroupChat(String credentials, UUID chatId) {

        final User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(IllegalArgumentException::new);



        return groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new);
    }

    public GroupChat createGroupChat(String creatorUsername, CreateGroupChatDto data) {
        final User creator = userRepository.findUserByUsername(creatorUsername)
                .orElseThrow(IllegalArgumentException::new);

        data.setCreator(creator.getId());
        final Set<User> invitedUsers = userRepository.findUsersByUsernameIn(CollectionUtils
                .emptyIfNull(data.getInvitedUsers()));

        final GroupChat groupChat = groupChatRepository.save(GroupChatDtoMapper.dtoToGroupChat(data, creator));

        groupChat.getUsers().addAll(invitedUsers);

        return groupChatRepository.save(groupChat);

    }

    public void deleteGroupChat(DeleteGroupDto chatId) {
        groupChatRepository.delete(groupChatRepository.findById(chatId.getId())
                .orElseThrow(IllegalArgumentException::new));
    }

    public void editGroupChat(String credentials, UUID chatId, EditGroupChatDto data) {
        User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(IllegalArgumentException::new);

        final GroupChat groupChat = groupChatRepository.findById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        if (isAdmin(user.getId(), groupChat.getId())) {
            final GroupChat groupChatToSave = EditDataDtoMapper.editDataDtoToGroupChat(data);
            groupChatRepository.save(groupChat.merge(groupChatToSave));
        } else {
            //  TODO : ADD NEW EXCEPTION
            throw new IllegalArgumentException();
        }
    }

    public User setAdminInGroupChat(String credentials, UUID chatId, UUID userId) throws IllegalAccessException {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new);

        if (!isAdmin(chatId, thirdPerson.getId())) {
            throw new IllegalAccessException();
        }

        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        if (isAdmin(chatId, userId)) {
            throw new IllegalArgumentException();
        }

        chat.getAdmins().add(user);

        groupChatRepository.save(chat);

        return user;
    }

    public User removeAdminInGroupChat(String credentials, UUID chatId, UUID userId) throws IllegalAccessException {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new);

        if (isAdmin(chatId, thirdPerson.getId())) {
            throw new IllegalAccessException();
        }

        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        if (isAdmin(chatId, userId)) {
            throw new IllegalArgumentException();
        }

        chat.getAdmins().remove(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User addUserInGroupChat(String credentials, UUID chatId, UUID userId) {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(IllegalArgumentException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new);


        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        if (isPresent(userId, chatId)) {
            throw new IllegalArgumentException();
        }

        chat.getUsers().add(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User deleteUserInGroupChat(String credentials, UUID chatId, UUID userId) {

        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(IllegalArgumentException::new);

        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        if (!isPresent(userId, chatId)) {
            throw new IllegalArgumentException();
        }

        chat.getAdmins().remove(user);
        groupChatRepository.save(chat);

        return user;
    }

    private Set<UUID> resolveIds(Collection<UUID> ids) {
        return CollectionUtils.emptyIfNull(ids).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private boolean isAdmin(UUID user, UUID chatId) {
        return groupChatRepository.isAdminInGroupChat(chatId, user);
    }

    private boolean isPresent(UUID user, UUID chatId) {
        if(!groupChatRepository.isPresentInChat(chatId, user)){
            throw new IllegalArgumentException();
        };
        return true;
    }



 /*   private boolean isPresentInChat(UUID chat_id, Set<UUID> user_id) {
        return groupChatRepository.isPresentInChat(chat_id, user_id);
    }*/

}
