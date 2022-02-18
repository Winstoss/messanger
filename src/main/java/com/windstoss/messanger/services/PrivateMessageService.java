package com.windstoss.messanger.services;


import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendDescribedFileMessageDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.api.mapper.MessageMapper;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class PrivateMessageService {

    //TODO: add filters to messages

    private String uploadPath;

    private final PrivateChatDescribedFileMessageRepository privateChatDescribedFileMessageRepository;

    private final UserRepository userRepository;

    private final PrivateChatRepository privateChatRepository;

    private final PrivateChatMessageSignatureRepository privateChatMessageSignatureRepository;

    private final PrivateChatFileMessageRepository privateChatFileMessageRepository;

    private final PrivateChatTextMessageRepository privateChatTextMessageRepository;

    private final MessageMapper messageMapper;

    public PrivateMessageService(
            @Value("${upload.path}") String uploadPath,
            PrivateChatDescribedFileMessageRepository privateChatDescribedFileMessageRepository,
            PrivateChatMessageSignatureRepository privateChatMessageSignatureRepository,
            PrivateChatTextMessageRepository privateChatTextMessageRepository,
            PrivateChatFileMessageRepository privateChatFileMessageRepository,
            PrivateChatRepository privateChatRepository,
            UserRepository userRepository,
            MessageMapper messageMapper) {

        this.uploadPath = Objects.requireNonNull(uploadPath);
        this.privateChatMessageSignatureRepository = Objects.requireNonNull(privateChatMessageSignatureRepository);
        this.privateChatTextMessageRepository = Objects.requireNonNull(privateChatTextMessageRepository);
        this.privateChatFileMessageRepository = Objects.requireNonNull(privateChatFileMessageRepository);
        this.privateChatDescribedFileMessageRepository = Objects.requireNonNull(privateChatDescribedFileMessageRepository);
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.messageMapper = Objects.requireNonNull(messageMapper);
    }


    public PrivateChatTextMessage sendPrivateTextMessage(String author, UUID chatId, SendTextMessageDto data) {

        final User sender = exists(author);

        final PrivateChat chat = requestedChatExists(sender.getId(), chatId);

        return privateChatTextMessageRepository.save(TextMessageDtoMapper.map(sender, chat, data));
    }

    public List<PrivateChatTextMessage> getAllTextMessages(User user, UUID chatId) {

        requestedChatExists(user.getId(), chatId);

        return privateChatTextMessageRepository.findMessagesInChat(chatId);
    }


    public PrivateChatTextMessage editPrivateTextMessage(String credentials,
                                                         UUID chatId,
                                                         UUID messageId,
                                                         EditTextMessageDto data) {
        final User author = exists(credentials);
        requestedChatExists(author.getId(), chatId);

        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (StringUtils.isEmpty(data.getText())) {
            throw new ContentIsEmptyException();
        }

        if (!author.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        return privateChatTextMessageRepository.save(message);
    }


    public void deletePrivateChatTextMessage(String credentials, UUID chatId, UUID messageId) {
        final User user = exists(credentials);
        final PrivateChat chat = requestedChatExists(user.getId(), chatId);
        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!user.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        privateChatTextMessageRepository.delete(message);
    }

    public List<String> sendMessageWithFile(SendMessageDto data) throws IOException {

        //TODO: to get rid of if-statement
        final User sender = exists(data.getAuthor());
        final PrivateChat chat = requestedChatExists(sender.getId(), data.getChatId());
        List<String> list = new ArrayList<String>();

        if(StringUtils.isEmpty(data.getText())){
            return sendFileMessage(data, list, sender, chat);
        } else {
            return sendDescribedFileMessage(data, list, sender, chat);
        }

    }

    private List<String> sendFileMessage(SendMessageDto data,
                                         List<String> list, User sender,
                                         PrivateChat chat) throws IOException{

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


            list.add(privateChatFileMessageRepository
                    .save(TextMessageDtoMapper.mapF(sender, chat, filePath))
                    .getFilePath());

        }

        return list;
    }

    private List<String> sendDescribedFileMessage(SendMessageDto data,
                                                  List<String> list,
                                                  User sender,
                                                  PrivateChat chat) throws IOException{
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
            list.add(privateChatDescribedFileMessageRepository
                    .save(TextMessageDtoMapper.mapDF(sender, chat, filePath, data.getText()))
                    .getFilePath());
        }

        return list;
    }

    private User exists(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private PrivateChat requestedChatExists(UUID userId, UUID chatId){
        return privateChatRepository.findChatById(chatId, userId).orElseThrow(ChatNotFoundException::new);
    }

}
