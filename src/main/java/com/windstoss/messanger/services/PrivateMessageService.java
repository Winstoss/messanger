package com.windstoss.messanger.services;


import com.windstoss.messanger.api.dto.Message.EditTextMessageDto;
import com.windstoss.messanger.api.dto.Message.SendMessageDto;
import com.windstoss.messanger.api.dto.Message.SendTextMessageDto;
import com.windstoss.messanger.api.mapper.MessageMapper;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    private final UserRepository userRepository;

    private final PrivateChatRepository privateChatRepository;

    private final PrivateChatMessageSignatureRepository privateChatMessageSignatureRepository;

    private final PrivateChatFileMessageRepository privateChatFileMessageRepository;

    private final PrivateChatTextMessageRepository privateChatTextMessageRepository;

    private final MessageMapper messageMapper;

    public PrivateMessageService(

            @Value("${upload.path}") String uploadPath,
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
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.messageMapper = Objects.requireNonNull(messageMapper);
    }


    public PrivateChatTextMessage sendPrivateTextMessage(String author, UUID chatId, SendTextMessageDto data) {

        final User sender = exists(author);

        final PrivateChat chat = privateChatRepository.findPrivateChatById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        if (!chat.getFirstUser().equals(sender) && !chat.getSecondUser().equals(sender)) {
            throw new IllegalArgumentException();
        }

        return privateChatTextMessageRepository.save(TextMessageDtoMapper.map(sender, chat, data));
    }

    public List<PrivateChatTextMessage> getAllTextMessages(String user, UUID chatId) {

        final User sender = exists(user);

        final PrivateChat chat = privateChatRepository.findPrivateChatById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        if (!chat.getFirstUser().equals(sender) && !chat.getSecondUser().equals(sender)) {
            throw new IllegalArgumentException();
        }

        return privateChatTextMessageRepository.findMessagesInChat(chatId);
    }


    public PrivateChatTextMessage editPrivateTextMessage(String credentials,
                                                         UUID chatId,
                                                         UUID messageId,
                                                         EditTextMessageDto data) {
        final User author = exists(credentials);
        final PrivateChat chat = privateChatRepository.findPrivateChatById(chatId)
                .orElseThrow(IllegalArgumentException::new);
        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        if (StringUtils.isEmpty(data.getText())) {
            throw new IllegalArgumentException();
        }

        if (!author.equals(message.getAuthor())) {
            throw new IllegalArgumentException();
        }

        if (!chat.getFirstUser().equals(author) && !chat.getSecondUser().equals(author)) {
            throw new IllegalArgumentException();
        }


        return privateChatTextMessageRepository.save(message);
    }


    public void deletePrivateChatTextMessage(String credentials, UUID chatId, UUID messageId) {
        final User user = exists(credentials);
        final PrivateChat chat = privateChatRepository.findPrivateChatById(chatId).orElseThrow(IllegalArgumentException::new);
        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(IllegalArgumentException::new);

        if (!user.equals(message.getAuthor())) {
            throw new IllegalArgumentException();
        }

        if (!chat.getFirstUser().equals(user) && !chat.getSecondUser().equals(user)) {
            throw new IllegalArgumentException();
        }

        privateChatTextMessageRepository.delete(message);
    }

    public List<String> sendFileMessage(String credentials, UUID chatId, SendMessageDto data) throws IOException {

        final User sender = exists(credentials);

        final PrivateChat chat = privateChatRepository.findPrivateChatById(chatId)
                .orElseThrow(IllegalArgumentException::new);

        if (!chat.getFirstUser().equals(sender) && !chat.getSecondUser().equals(sender)) {
            //TODO: add new exception handler / exceptions
            throw new IllegalArgumentException();
        }

        //TODO: refactor this
        final MultipartFile file = data.getFile();

        List<String> list = new ArrayList<String>();

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            file.transferTo(new File(filePath));


            list.add(privateChatFileMessageRepository.save(TextMessageDtoMapper.mapF(sender, chat, filePath)).getFilePath());
        }

        list.add(privateChatTextMessageRepository.save(TextMessageDtoMapper.map(sender, chat, data)).getContent());

        return list;
    }

    private User exists(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(IllegalArgumentException::new);
    }

}
