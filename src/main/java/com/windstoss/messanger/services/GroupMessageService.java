package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.GroupChatTextMessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.api.mapper.GroupChatTextMessageRetrievalDtoMapper;
import com.windstoss.messanger.api.mapper.MessageMapper;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupMessageService {

    private String uploadPath;

    private final MessageMapper messageMapper;

    private final UserRepository userRepository;

    private final GroupChatRepository groupChatRepository;

    private final GroupChatMessageSignatureRepository groupChatMessageSignatureRepository;

    private final GroupChatTextMessageRepository groupChatTextMessageRepository;

    private final GroupChatFileMessageRepository groupChatFileMessageRepository;

    private final GroupChatDescribedFileMessageRepository groupChatDescribedFileMessageRepository;

    public GroupMessageService(@Value("${upload.path}") String uploadPath,
                               UserRepository userRepository,
                               GroupChatRepository groupChatRepository,
                               GroupChatMessageSignatureRepository groupChatMessageSignatureRepository,
                               GroupChatTextMessageRepository groupChatTextMessageRepository,
                               GroupChatFileMessageRepository groupChatFileMessageRepository,
                               GroupChatDescribedFileMessageRepository groupChatDescribedFileMessageRepository,
                               MessageMapper messageMapper) {
        this.uploadPath = Objects.requireNonNull(uploadPath);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.groupChatMessageSignatureRepository = Objects.requireNonNull(groupChatMessageSignatureRepository);
        this.groupChatTextMessageRepository = Objects.requireNonNull(groupChatTextMessageRepository);
        this.groupChatFileMessageRepository = Objects.requireNonNull(groupChatFileMessageRepository);
        this.groupChatDescribedFileMessageRepository = Objects.requireNonNull(groupChatDescribedFileMessageRepository);
        this.messageMapper = Objects.requireNonNull(messageMapper);
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

        final User user = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);

        if(!groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new).getUsers().contains(user)){
            throw new UserAbsenceException();
        };

        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (StringUtils.isEmpty(data.getText())) {
            throw new IllegalArgumentException();
        }

        if (user != message.getAuthor() || !groupChatRepository.isAdminInGroupChat(chatId, user.getId())) {
            throw new GroupChatPrivilegesException();
        }

        message.setContent(data.getText());
        return GroupChatTextMessageRetrievalDtoMapper.map(groupChatTextMessageRepository.save(message));
    }

    public void deleteGroupChatTextMessage(String credentials, UUID chatId, UUID messageId) {

        final User user = userRepository.findUserByUsername(credentials).orElseThrow(UserNotFoundException::new);

        if (!groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new).getUsers().contains(user)){
            throw new UserAbsenceException();
        };
        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (user != message.getAuthor() || !groupChatRepository.isAdminInGroupChat(chatId, user.getId())) {
            throw new GroupChatPrivilegesException();
        }

        groupChatTextMessageRepository.delete(message);
    }

    public List<String> sendMessageWithFile(SendMessageDto data) throws IOException {
        //TODO: to get rid of if-statement
        final User sender = exists(data.getAuthor());
        final GroupChat chat = requestedChatExists(sender.getId(), data.getChatId());
        List<String> list = new ArrayList<String>();

        if(StringUtils.isEmpty(data.getText())){
            return sendFileMessage(data, list, sender, chat);
        } else {
            return sendDescribedFileMessage(data, list, sender, chat);
        }

    }

    private List<String> sendFileMessage(SendMessageDto data,
                                         List<String> list, User sender,
                                         GroupChat chat) throws IOException{

        final MultipartFile file = data.getFile();

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            file.transferTo(new File(filePath));


            list.add(groupChatFileMessageRepository
                    .save(TextMessageDtoMapper.mapGF(sender, chat, filePath))
                    .getFilePath());

        }

        return list;
    }

    private List<String> sendDescribedFileMessage(SendMessageDto data,
                                                  List<String> list,
                                                  User sender,
                                                  GroupChat chat) throws IOException{
        final MultipartFile file = data.getFile();

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            file.transferTo(new File(filePath));

            list.add(data.getText());
            list.add(groupChatDescribedFileMessageRepository
                    .save(TextMessageDtoMapper.mapGDF(sender, chat, filePath, data.getText()))
                    .getFilePath());
        }

        return list;
    }

    private User exists(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private GroupChat requestedChatExists(UUID userId, UUID chatId){

        GroupChat chat = groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if(groupChatRepository.isPresentInChat(chatId, userId)) {
            return groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        }
        else throw new UserAbsenceException();
    }
}
