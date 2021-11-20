package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.api.mapper.EditDataDtoMapper;
import com.windstoss.messanger.api.mapper.GroupChatDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.GroupChatRepository;
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
public class GroupChatService {
    private final UserRepository userRepository;

    private final GroupChatRepository groupChatRepository;

    public GroupChatService(GroupChatRepository groupChatRepository,
                            UserRepository userRepository) {
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
    }


    public GroupChat getGroupChat(String credentials, UUID chatId) {

        final User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(UserNotFoundException::new);

        return groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
    }

    public GroupChat createGroupChat(String creatorUsername, CreateGroupChatDto data) {
        final User creator = userRepository.findUserByUsername(creatorUsername)
                .orElseThrow(UserNotFoundException::new);

        data.setCreator(creator.getId());
        final Set<User> invitedUsers = userRepository.findUsersByUsernameIn(CollectionUtils
                .emptyIfNull(data.getInvitedUsers()));

        final GroupChat groupChat = groupChatRepository.save(GroupChatDtoMapper.dtoToGroupChat(data, creator));

        groupChat.getUsers().addAll(invitedUsers);

        return groupChatRepository.save(groupChat);

    }

    public void deleteGroupChat(DeleteGroupDto chatId) {
        groupChatRepository.delete(groupChatRepository.findById(chatId.getId())
                .orElseThrow(ChatNotFoundException::new));
    }

    public void editGroupChat(String credentials, UUID chatId, EditGroupChatDto data) {
        User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(UserNotFoundException::new);

        final GroupChat groupChat = groupChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);

        if (isAdmin(user.getId(), groupChat.getId())) {
            final GroupChat groupChatToSave = EditDataDtoMapper.editDataDtoToGroupChat(data);
            groupChatRepository.save(groupChat.merge(groupChatToSave));
        } else {

            throw new GroupChatPrivilegesException();
        }
    }

    public User setAdminInGroupChat(String credentials, UUID chatId, UUID userId) throws IllegalAccessException {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (!isAdmin(chatId, thirdPerson.getId())) {
            throw new GroupChatPrivilegesException();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (isAdmin(chatId, userId)) {
            throw new UserIsAlreadyAdminException();
        }

        chat.getAdmins().add(user);

        groupChatRepository.save(chat);

        return user;
    }

    public User removeAdminInGroupChat(String credentials, UUID chatId, UUID userId) throws IllegalAccessException {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (isAdmin(chatId, thirdPerson.getId())) {
            throw new GroupChatPrivilegesException();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (isAdmin(chatId, userId)) {
            throw new GroupChatPrivilegesException();
        }

        chat.getAdmins().remove(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User addUserInGroupChat(String credentials, UUID chatId, UUID userId) {

        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserAbsenceException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);


        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (isPresent(userId, chatId)) {
            throw new UserIsAlreadyPresentException();
        }

        chat.getUsers().add(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User deleteUserInGroupChat(String credentials, UUID chatId, UUID userId) {

        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!isPresent(userId, chatId)) {
            throw new UserAbsenceException();
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
        if (!groupChatRepository.isPresentInChat(chatId, user)) {
            throw new IllegalArgumentException();
        }

        return true;
    }


}
