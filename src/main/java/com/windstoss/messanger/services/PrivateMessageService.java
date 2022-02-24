package com.windstoss.messanger.services;


import com.windstoss.messanger.api.dto.Message.*;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.api.mapper.TextMessageDtoMapper;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatFileMessage;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class PrivateMessageService {

    //TODO: add filters to messages

    private String uploadPath;

    private final PrivateChatDescribedFileMessageRepository privateChatDescribedFileMessageRepository;

    private final PrivateChatRepository privateChatRepository;

    private final PrivateChatFileMessageRepository privateChatFileMessageRepository;

    private final PrivateChatTextMessageRepository privateChatTextMessageRepository;


    public PrivateMessageService(
            @Value("${upload.path}") String uploadPath,
            PrivateChatDescribedFileMessageRepository privateChatDescribedFileMessageRepository,
            PrivateChatTextMessageRepository privateChatTextMessageRepository,
            PrivateChatFileMessageRepository privateChatFileMessageRepository,
            PrivateChatRepository privateChatRepository) {

        this.uploadPath = Objects.requireNonNull(uploadPath);
        this.privateChatTextMessageRepository = Objects.requireNonNull(privateChatTextMessageRepository);
        this.privateChatFileMessageRepository = Objects.requireNonNull(privateChatFileMessageRepository);
        this.privateChatDescribedFileMessageRepository = Objects.requireNonNull(privateChatDescribedFileMessageRepository);
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
    }

    public List<MessageRetrievalDto> getAllTextMessages(User user, UUID chatId) {

        requestedChatExists(user.getId(), chatId);

        List<MessageRetrievalDto> textMessages = privateChatDescribedFileMessageRepository.findAllByChatId(chatId)
                .stream()
                    .map(it -> MessageRetrievalDto.builder()
                            .messageId(it.getId())
                            .nickname(it.getAuthor().getNickname())
                            .text(it.getDescription())
                            .file(it.getFilePath())
                            .build())
                .collect(Collectors.toList());

        List<MessageRetrievalDto> fileMessages = privateChatTextMessageRepository.findMessagesInChat(chatId)
                .stream()
                .map(it -> MessageRetrievalDto.builder()
                        .messageId(it.getId())
                        .nickname(it.getAuthor().getNickname())
                        .text(it.getContent())
                        .file(null)
                        .build())
                .collect(Collectors.toList());

        List<MessageRetrievalDto> describedMessages = privateChatFileMessageRepository.findFileMessagesByChat(chatId)
                .stream()
                .map(it -> MessageRetrievalDto.builder()
                        .messageId(it.getId())
                        .nickname(it.getAuthor().getNickname())
                        .text(null)
                        .file(it.getFilePath())
                        .build())
                .collect(Collectors.toList());

        for (MessageRetrievalDto a : describedMessages) {
            fileMessages.removeIf(it -> it.getMessageId() == a.getMessageId());
        }


        textMessages.addAll(fileMessages);
        textMessages.addAll(describedMessages);

        return textMessages;
    }


    public MessageRetrievalDto sendPrivateTextMessage(User sender, UUID chatId, String text) {

        if(StringUtils.isEmpty(text)){
            throw new MessageIsEmptyException();
        }

        final PrivateChat chat = requestedChatExists(sender.getId(), chatId);

        PrivateChatTextMessage message = privateChatTextMessageRepository.save(PrivateChatTextMessage.builder()
                        .content(text)
                        .author(sender)
                        .chat(chat)
                        .build());
        
        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(text)
                .file(null)
                .nickname(sender.getNickname())
                .build();
    }

    public MessageRetrievalDto sendPrivateFileMessage(User sender, UUID chatId, MultipartFile file) throws IOException {

        final PrivateChat chat = requestedChatExists(sender.getId(), chatId);

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "." + file.getOriginalFilename();
        String filePath = uploadPath + "\\" + fileName;

        file.transferTo(new File(filePath));
        PrivateChatFileMessage message = privateChatFileMessageRepository.save(PrivateChatFileMessage.builder()
                .filePath(filePath)
                .chat(chat)
                .author(sender)
                .build());

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .messageId(message.getId())
                .text(null)
                .build();
    }

    public MessageRetrievalDto sendPrivateDescribedFileMessage(User sender,
                                                               UUID chatId,
                                                               MultipartFile file,
                                                               String text) throws IOException {
        final PrivateChat chat = requestedChatExists(sender.getId(), chatId);

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "." + file.getOriginalFilename();
        String filePath = uploadPath + "\\" + fileName;

        file.transferTo(new File(filePath));

        PrivateChatDescribedFileMessage message = privateChatDescribedFileMessageRepository
                .save(PrivateChatDescribedFileMessage.builder()
                        .description(text)
                        .author(sender)
                        .filePath(filePath)
                        .chat(chat)
                        .build());


        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(message.getDescription())
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .build();
    }

    public MessageRetrievalDto editPrivateTextMessage(User editor,
                                                      UUID chatId,
                                                      UUID messageId,
                                                      String text) {
        requestedChatExists(editor.getId(), chatId);

        PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!editor.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }
        
        message.setContent(text);
        privateChatTextMessageRepository.save(message);
        
        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .file(null)
                .text(text)
                .nickname(editor.getNickname())
                .build();
    }

    public MessageRetrievalDto editPrivateFileMessage(User editor,
                                                      UUID chatId,
                                                      UUID messageId,
                                                      MultipartFile file) throws IOException {

        requestedChatExists(editor.getId(), chatId);
        PrivateChatFileMessage message = privateChatFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if(file == null && file.isEmpty()){
            throw new MessageIsEmptyException();
        }

        if (!message.getAuthor().equals(editor)){
            throw new MessageAuthorityException();
        }

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "." + file.getOriginalFilename();
        String filePath = uploadPath + "\\" + fileName;

        if (message.getFilePath().equals(filePath)){
            throw new MessageIsEqualException();
        }

        Files.delete(Paths.get(message.getFilePath()));
        file.transferTo(new File(filePath));
        message.setFilePath(filePath);
        privateChatFileMessageRepository.save(message);

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .messageId(message.getId())
                .text(null)
                .build();
    }

    public MessageRetrievalDto editPrivateDescribedFileMessage(User editor,
                                                               UUID chatId,
                                                               UUID messageId,
                                                               MultipartFile file,
                                                               String text) throws IOException {
        requestedChatExists(editor.getId(), chatId);
        PrivateChatDescribedFileMessage message = privateChatDescribedFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!message.getAuthor().equals(editor)){
            throw new MessageAuthorityException();
        }

        if(StringUtils.isEmpty(text) && (file == null || file.isEmpty())){
            throw new MessageIsEmptyException();
        }

        if(file != null && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            if (message.getFilePath().equals(filePath)) {
                throw new MessageIsEqualException();
            }
            Files.delete(Paths.get(message.getFilePath()));
            file.transferTo(new File(filePath));
            message.setFilePath(filePath);
        }

        message.setDescription(StringUtils.defaultIfEmpty(text, message.getDescription()));
        privateChatFileMessageRepository.save(message);

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .messageId(message.getId())
                .text(text)
                .build();
    }


    public boolean deleteTextMessage(User requester, UUID chatId, UUID messageId) {

        requestedChatExists(requester.getId(), chatId);
        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        privateChatTextMessageRepository.delete(message);

        return true;
    }


    public boolean deleteFileMessage(User requester, UUID chatId, UUID messageId) throws IOException {

        requestedChatExists(requester.getId(), chatId);
        final PrivateChatFileMessage message = privateChatFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        privateChatFileMessageRepository.delete(message);

        return true;
    }

    public boolean deleteDescribedFileMessage(User requester, UUID chatId, UUID messageId) throws IOException {

        requestedChatExists(requester.getId(), chatId);
        final PrivateChatDescribedFileMessage message = privateChatDescribedFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        privateChatFileMessageRepository.delete(message);

        return true;

    }

    private PrivateChat requestedChatExists(UUID userId, UUID chatId){
        return privateChatRepository.findChatById(chatId, userId).orElseThrow(ChatNotFoundException::new);
    }


}
