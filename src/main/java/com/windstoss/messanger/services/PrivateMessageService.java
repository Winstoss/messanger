package com.windstoss.messanger.services;


import com.windstoss.messanger.api.dto.Message.*;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.domain.Chats.PrivateChat;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatFileMessage;
import com.windstoss.messanger.domain.Messages.PrivateMessages.PrivateChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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

    private final ApplicationEventPublisher publisher;

    public PrivateMessageService(
            @Value("${upload.path}") String uploadPath,
            ApplicationEventPublisher publisher,
            PrivateChatDescribedFileMessageRepository privateChatDescribedFileMessageRepository,
            PrivateChatTextMessageRepository privateChatTextMessageRepository,
            PrivateChatFileMessageRepository privateChatFileMessageRepository,
            PrivateChatRepository privateChatRepository) {

        this.uploadPath = Objects.requireNonNull(uploadPath);
        this.publisher = publisher;
        this.privateChatTextMessageRepository = Objects.requireNonNull(privateChatTextMessageRepository);
        this.privateChatFileMessageRepository = Objects.requireNonNull(privateChatFileMessageRepository);
        this.privateChatDescribedFileMessageRepository = Objects.requireNonNull(privateChatDescribedFileMessageRepository);
        this.privateChatRepository = Objects.requireNonNull(privateChatRepository);
    }

    public List<MessageRetrievalDto> getAllPrivateMessages(User requester, UUID userId) {

        UUID chatId = requestedChatExists(requester.getId(), userId).getId();

        List<MessageRetrievalDto> describedMessages = privateChatDescribedFileMessageRepository.findAllByChatId(chatId)
                .stream()
                    .map(it -> MessageRetrievalDto.builder()
                            .messageId(it.getId())
                            .nickname(it.getAuthor().getNickname())
                            .text(it.getDescription())
                            .file(it.getFilePath())
                            .chatId(chatId)
                            .build())
                .collect(Collectors.toList());

        List<MessageRetrievalDto> textMessages = privateChatTextMessageRepository.findMessagesInChat(chatId)
                .stream()
                .map(it -> MessageRetrievalDto.builder()
                        .messageId(it.getId())
                        .nickname(it.getAuthor().getNickname())
                        .text(it.getContent())
                        .file(null)
                        .chatId(chatId)
                        .build())
                .collect(Collectors.toList());

        List<MessageRetrievalDto> fileMessages = privateChatFileMessageRepository.findFileMessagesByChat(chatId)
                .stream()
                .map(it -> MessageRetrievalDto.builder()
                        .messageId(it.getId())
                        .nickname(it.getAuthor().getNickname())
                        .text(null)
                        .file(it.getFilePath())
                        .chatId(chatId)
                        .build())
                .collect(Collectors.toList());

        for (MessageRetrievalDto a : describedMessages) {
            fileMessages.removeIf(it -> it.getMessageId() == a.getMessageId());
        }

        textMessages.addAll(fileMessages);
        textMessages.addAll(describedMessages);

        return textMessages;
    }


    public MessageRetrievalDto sendPrivateTextMessage(User sender, UUID userId, String text) {

        if(StringUtils.isEmpty(text)){
            throw new MessageIsEmptyException();
        }

        final PrivateChat chat = requestedChatExists(sender.getId(), userId);

        PrivateChatTextMessage message = privateChatTextMessageRepository.save(PrivateChatTextMessage.builder()
                        .content(text)
                        .author(sender)
                        .chat(chat)
                        .build());

        publisher.publishEvent(MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(text)
                .file(null)
                .nickname(sender.getNickname())
                .chatId(chat.getId())
                .build());

        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(text)
                .file(null)
                .nickname(sender.getNickname())
                .chatId(chat.getId())
                .build();
    }

    public MessageRetrievalDto sendPrivateFileMessage(User sender, UUID userId, MultipartFile file) throws IOException {

        final PrivateChat chat = requestedChatExists(sender.getId(), userId);

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

        publisher.publishEvent(MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .messageId(message.getId())
                .text(null)
                .chatId(chat.getId())
                .build());

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .messageId(message.getId())
                .text(null)
                .chatId(chat.getId())
                .build();
    }

    public MessageRetrievalDto sendPrivateDescribedFileMessage(User sender,
                                                               UUID userId,
                                                               MultipartFile file,
                                                               String text) throws IOException {
        final PrivateChat chat = requestedChatExists(sender.getId(), userId);

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


        publisher.publishEvent(MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(message.getDescription())
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .chatId(chat.getId())
                .build());

        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(message.getDescription())
                .file(message.getFilePath())
                .nickname(sender.getNickname())
                .chatId(chat.getId())
                .build();
    }

    public MessageRetrievalDto editPrivateTextMessage(User editor,
                                                      UUID userId,
                                                      UUID messageId,
                                                      String text) {
        UUID chatId =requestedChatExists(editor.getId(), userId).getId();

        PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!editor.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }
        
        message.setContent(text);
        privateChatTextMessageRepository.save(message);

        publisher.publishEvent(MessageRetrievalDto.builder()
                .messageId(message.getId())
                .file(null)
                .text(text)
                .nickname(editor.getNickname())
                .chatId(chatId)
                .build());
        
        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .file(null)
                .text(text)
                .nickname(editor.getNickname())
                .chatId(chatId)
                .build();
    }

    public MessageRetrievalDto editPrivateFileMessage(User editor,
                                                      UUID userId,
                                                      UUID messageId,
                                                      MultipartFile file) throws IOException {

        UUID chatId = requestedChatExists(editor.getId(), userId).getId();
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

        publisher.publishEvent(MessageRetrievalDto.builder()
                .messageId(message.getId())
                .file(message.getFilePath())
                .text(null)
                .nickname(editor.getNickname())
                .chatId(chatId)
                .build());

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .messageId(message.getId())
                .text(null)
                .chatId(chatId)
                .build();
    }

    public MessageRetrievalDto editPrivateDescribedFileMessage(User editor,
                                                               UUID userId,
                                                               UUID messageId,
                                                               MultipartFile file,
                                                               String text) throws IOException {
        UUID chatId = requestedChatExists(editor.getId(), userId).getId();
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
        publisher.publishEvent(MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .messageId(message.getId())
                .text(text)
                .chatId(chatId)
                .build());

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .messageId(message.getId())
                .text(text)
                .chatId(chatId)
                .build();
    }


    public DeleteMessageRetrievalDto deleteTextMessage(User requester, UUID userId, UUID messageId) {

        UUID chatId = requestedChatExists(requester.getId(), userId).getId();
        final PrivateChatTextMessage message = privateChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        privateChatTextMessageRepository.delete(message);
        publisher.publishEvent(DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build());

        return DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }


    public DeleteMessageRetrievalDto deleteFileMessage(User requester, UUID userID, UUID messageId) throws IOException {

        UUID chatId = requestedChatExists(requester.getId(), userID).getId();
        final PrivateChatFileMessage message = privateChatFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        privateChatFileMessageRepository.delete(message);
        publisher.publishEvent(DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build());

        return DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    public DeleteMessageRetrievalDto deleteDescribedFileMessage(User requester, UUID userId, UUID messageId) throws IOException {

        UUID chatId = requestedChatExists(requester.getId(), userId).getId();
        final PrivateChatDescribedFileMessage message = privateChatDescribedFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (!requester.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        privateChatFileMessageRepository.delete(message);
        publisher.publishEvent(DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build());

        return DeleteMessageRetrievalDto.builder()
                .isDeleted(true)
                .chatId(chatId)
                .messageId(messageId)
                .build();

    }

    private PrivateChat requestedChatExists(UUID requesterId, UUID userId){
        return privateChatRepository.findByUsersId(requesterId, userId).orElseThrow(ChatNotFoundException::new);
    }


}
