package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.GroupChat.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.DeleteGroupDto;
import com.windstoss.messanger.api.dto.GroupChat.EditGroupChatDto;
import com.windstoss.messanger.api.dto.GroupChat.ManageUserInGroupChatDto;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.api.mapper.GroupChatDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.GroupChatRepository;
import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class GroupChatService {

    private final String uploadPath;

    private final UserRepository userRepository;

    private final GroupChatRepository groupChatRepository;

    public GroupChatService(@Value("${upload.path}") String uploadPath,
                            GroupChatRepository groupChatRepository,
                            UserRepository userRepository) {
        this.uploadPath = Objects.requireNonNull(uploadPath);
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

    public void editGroupChat(String credentials, UUID chatId, EditGroupChatDto data)  {

        final User user = userRepository.findUserByUsername(credentials)
                .orElseThrow(UserNotFoundException::new);

        final GroupChat groupChat = groupChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);

        String filePath = null;

        if(data.getImage()!= null && !data.getImage().isEmpty()){

            final MultipartFile file = data.getImage();

             File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            filePath = uploadPath + "\\" + fileName;

            try {
                file.transferTo(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!StringUtils.isEmpty(groupChat.getImagePath())){
                uploadDir = new File(groupChat.getImagePath());
                uploadDir.delete();

            }
        }

        if (isAdmin(user.getId(), groupChat.getId())) {
            groupChatRepository.save(groupChat.merge(GroupChat.builder()
                    .imagePath(filePath)
                    .title(data.getTitle())
                    .build()));
        } else {
            throw new GroupChatPrivilegesException();
        }
    }

    public User setAdminInGroupChat(String credentials, UUID chatId, ManageUserInGroupChatDto data)  {

        final UUID userId = data.getId();
        final User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);
        final GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (!isAdmin(thirdPerson.getId(), chatId)) {
            throw new GroupChatPrivilegesException();
        }

        final User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (isAdmin(user.getId(), chatId)) {
            throw new UserIsAlreadyAdminException();
        }

        chat.getAdmins().add(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User removeAdminInGroupChat(String credentials, UUID chatId, ManageUserInGroupChatDto data) {

        UUID userId = data.getId();
        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (!isAdmin(thirdPerson.getId(), chatId)) {
            throw new GroupChatPrivilegesException();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!isAdmin(userId, chatId)) {
            throw new GroupChatPrivilegesException();
        }

        chat.getAdmins().remove(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User addUserInGroupChat(String credentials, UUID chatId, ManageUserInGroupChatDto data) {

        UUID userId = data.getId();
        User thirdPerson = userRepository.findUserByUsername(credentials).orElseThrow(UserAbsenceException::new);
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!isPresent(thirdPerson.getId(), chatId)) {
            throw new UserAbsenceException();
        }

        if (isPresent(userId, chatId)) {
            throw new UserIsAlreadyPresentException();
        }

        chat.getUsers().add(user);
        groupChatRepository.save(chat);

        return user;
    }

    public User deleteUserInGroupChat(String credentials, UUID chatId, ManageUserInGroupChatDto data) {

        UUID userId = data.getId();
        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User requester = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);

        if (!isPresent(userId, chatId)) {
            throw new UserAbsenceException();
        }

        if (!isAdmin(requester.getId(), chatId)) {
            throw new GroupChatPrivilegesException();
        }

        if (isAdmin(requester.getId(), chatId) && requester.equals(user)){
            chat.getUsers().remove(user);
            groupChatRepository.save(chat);
            return user;
        }

        return requester;
    }

//    private Set<UUID> resolveIds(Collection<UUID> ids) {
//        return CollectionUtils.emptyIfNull(ids).stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//    }

    private boolean isAdmin(UUID user, UUID chatId) {
        return groupChatRepository.isAdminInGroupChat(chatId, user);
    }

    private boolean isPresent(UUID user, UUID chatId) {
        if (!groupChatRepository.isPresentInChat(chatId, user)) {
            return false;
        }

        return true;
    }


}
