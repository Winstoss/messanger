package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.CreateGroupChatDto;
import com.windstoss.messanger.api.dto.EditGroupChatDto;
import com.windstoss.messanger.api.mapper.EditDataDtoMapper;
import com.windstoss.messanger.api.mapper.GroupChatDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.GroupChatRepository;
import com.windstoss.messanger.repositories.PrivateChatRepository;
import com.windstoss.messanger.repositories.UserRepository;
import com.windstoss.messanger.utils.StringUtils;
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

    public void createPrivateChat(String firstUser, String secondUser) {
        User user1 = userRepository.findByUsername(firstUser)
                .orElseThrow(IllegalArgumentException::new);
        User user2 = userRepository.findByUsername(secondUser)
                .orElseThrow(IllegalArgumentException::new);

        privateChatRepository.findByUsersId(user1.getId(), user2.getId()).ifPresent(x -> {
            throw new IllegalArgumentException();
        });

        privateChatRepository.save(PrivateChat.builder()
                .firstUser(user1)
                .secondUser(user2)
                .build());
    }

    public void deletePrivateChat(String firstUser, String secondUser) {
        User user1 = userRepository.findByUsername(firstUser)
                .orElseThrow(IllegalArgumentException::new);
        User user2 = userRepository.findByUsername(secondUser)
                .orElseThrow(IllegalArgumentException::new);

        privateChatRepository.findByUsersId(user1.getId(), user2.getId())
                .orElseThrow(IllegalArgumentException::new);

        privateChatRepository.delete(PrivateChat.builder()
                .firstUser(user1)
                .secondUser(user2)
                .build());
    }

    public void createGroupChat(String creator, CreateGroupChatDto data) {
        User user = userRepository.findByUsername(creator)
                .orElseThrow(IllegalArgumentException::new);

        data.setCreator(user.getId());

        groupChatRepository.save(GroupChatDtoMapper.dtoToGroupChat(data));

    }

    public void deleteGroupChat(UUID chatId) {
        groupChatRepository.delete(groupChatRepository.findById(chatId)
                .orElseThrow(IllegalArgumentException::new));
    }

    public void editGroupChat(String credentials, EditGroupChatDto data) {
        User user = userRepository.findByUsername(credentials)
                .orElseThrow(IllegalArgumentException::new);

        final GroupChat groupChat = groupChatRepository.findById(data.getId())
                .orElseThrow(IllegalArgumentException::new);

        final Set<UUID> adminsIds = resolveIds(data.getAdmins());
        final Set<UUID> usersIds = resolveIds(data.getUsers());

        final Set<User> admins = userRepository.findByIdIn(adminsIds);
        final Set<User> users = userRepository.findByIdIn(usersIds);

        if (isAdmin(user.getId(), groupChat.getId())) {
            final GroupChat groupChatToSave = EditDataDtoMapper.editDataDtoToGroupChat(data, users, admins);
            groupChatRepository.save(groupChat.merge(groupChatToSave));
        } else if (!StringUtils.isEmpty(data.getDescription())) {
            final GroupChat groupChatToSave = EditDataDtoMapper.editDataDtoToGroupChat(data, users, admins);
            if (groupChatToSave.getUsers().isEmpty() || groupChatToSave.getAdmins().isEmpty()) {
                groupChatRepository.save(groupChatToSave);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            //  TODO : ADD NEW EXCEPTION
            throw new IllegalArgumentException();
        } 

    }

    private Set<UUID> resolveIds(Collection<UUID> ids) {
        return CollectionUtils.emptyIfNull(ids).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private boolean isAdmin(UUID user, UUID ChatId) {
        return groupChatRepository.isAdminInGroupChat(ChatId, user);
    }


}
