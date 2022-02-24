package com.windstoss.messanger.services;

import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.api.exception.exceptions.*;
import com.windstoss.messanger.domain.Chats.GroupChat;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatDescribedFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatFileMessage;
import com.windstoss.messanger.domain.Messages.GroupMessages.GroupChatTextMessage;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.repositories.*;
import com.windstoss.messanger.utils.StringUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
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
@Transactional(rollbackFor = Exception.class)
public class GroupMessageService {

    private final String uploadPath;

    private final GroupChatRepository groupChatRepository;

    private final GroupChatTextMessageRepository groupChatTextMessageRepository;

    private final GroupChatFileMessageRepository groupChatFileMessageRepository;

    private final GroupChatDescribedFileMessageRepository groupChatDescribedFileMessageRepository;

    public GroupMessageService(@Value("${upload.path}") String uploadPath,
                               GroupChatRepository groupChatRepository,
                               GroupChatTextMessageRepository groupChatTextMessageRepository,
                               GroupChatFileMessageRepository groupChatFileMessageRepository,
                               GroupChatDescribedFileMessageRepository groupChatDescribedFileMessageRepository) {
        this.uploadPath = Objects.requireNonNull(uploadPath);
        this.groupChatRepository = Objects.requireNonNull(groupChatRepository);
        this.groupChatTextMessageRepository = Objects.requireNonNull(groupChatTextMessageRepository);
        this.groupChatFileMessageRepository = Objects.requireNonNull(groupChatFileMessageRepository);
        this.groupChatDescribedFileMessageRepository = Objects.requireNonNull(groupChatDescribedFileMessageRepository);
    }


    public List<MessageRetrievalDto> getAllMessages(User user, UUID chatId) {


        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
        if(!groupChatRepository.isPresentInChat(chatId, user.getId())){
            throw new UserAbsenceException();
        }


        List<MessageRetrievalDto> messages = groupChatTextMessageRepository.getAllMessagesInChat(chatId)
                .stream().filter(Objects::nonNull).map((message) -> MessageRetrievalDto.builder()
                        .messageId(message.getId())
                        .nickname(message.getAuthor().getNickname())
                        .text(message.getContent())
                        .file("")
                        .build()).collect(Collectors.toList());

        List<MessageRetrievalDto> fileMessages = groupChatFileMessageRepository.findAllByChatId(chatId)
                .stream().filter(Objects::nonNull).map((message) -> MessageRetrievalDto.builder()
                        .messageId(message.getId())
                        .nickname(message.getAuthor().getNickname())
                        .text("")
                        .file(message.getFilePath())
                        .build()).collect(Collectors.toList());

        List<MessageRetrievalDto> dFileMessages = groupChatDescribedFileMessageRepository.findAllByChatId(chatId)
                .stream().filter(Objects::nonNull).map((message) -> MessageRetrievalDto.builder()
                        .messageId(message.getId())
                        .nickname(message.getAuthor().getNickname())
                        .text(message.getDescription())
                        .file(message.getFilePath())
                        .build()).collect(Collectors.toList());

        for (MessageRetrievalDto a : dFileMessages) {
            fileMessages.removeIf(it -> it.getMessageId() == a.getMessageId());
        }


        return ListUtils.union(messages, ListUtils.union(dFileMessages, fileMessages));
    }

    public MessageRetrievalDto sendTextMessage(User sender, UUID chatId, String message) {

        if(StringUtils.isEmpty(message)){
            throw new MessageIsEmptyException();
        }

        final GroupChat chat = groupChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);

        if (!chat.getUsers().contains(sender)) {
            throw new UserAbsenceException();
        }

        GroupChatTextMessage savedMessage = groupChatTextMessageRepository.save(GroupChatTextMessage.builder()
                .content(message)
                .author(sender)
                .chat(chat)
                .build());

        return MessageRetrievalDto.builder()
                .text(savedMessage.getContent())
                .messageId(savedMessage.getId())
                .nickname(savedMessage.getAuthor().getNickname())
                .file(null)
                .build();
    }

    public MessageRetrievalDto sendFileMessage(User sender,
                                               UUID chatId,
                                               MultipartFile file) throws IOException {

        final GroupChat chat = groupChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "." + file.getOriginalFilename();
        String filePath = uploadPath + "\\" + fileName;

        file.transferTo(new File(filePath));
        GroupChatFileMessage message = groupChatFileMessageRepository.save(GroupChatFileMessage.builder()
                .filePath(filePath)
                .chat(chat)
                .author(sender)
                .build());

        return MessageRetrievalDto.builder()
                .file(message.getFilePath())
                .nickname(message.getAuthor().getNickname())
                .messageId(message.getId())
                .text(null)
                .build();
    }

    public MessageRetrievalDto sendDescribedFileMessage(User sender,
                                                        UUID chatId,
                                                        MultipartFile file,
                                                        String text) throws IOException {

        final GroupChat chat = groupChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "." + file.getOriginalFilename();
        String filePath = uploadPath + "\\" + fileName;

        file.transferTo(new File(filePath));

        GroupChatDescribedFileMessage message = groupChatDescribedFileMessageRepository
                .save(GroupChatDescribedFileMessage.builder()
                        .description(text)
                        .author(sender)
                        .filePath(filePath)
                        .chat(chat)
                        .build());


        return MessageRetrievalDto.builder()
                .messageId(message.getId())
                .text(message.getDescription())
                .file(message.getFilePath())
                .nickname(message.getAuthor().getNickname())
                .build();
    }

    public MessageRetrievalDto editTextMessage(User editor,
                                               UUID chatId,
                                               UUID messageId,
                                               String newText) {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        if (StringUtils.isEmpty(newText)) {
            throw new MessageIsEmptyException();
        }

        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (message.getContent().equals(newText)) {
            throw new MessageIsEqualException();
        }

        if (!editor.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        message.setContent(newText);
        groupChatTextMessageRepository.save(message);

        return MessageRetrievalDto.builder()
                .nickname(editor.getNickname())
                .text(newText)
                .file(null)
                .messageId(message.getId())
                .build();
    }

    public MessageRetrievalDto editFileMessage(User editor,
                                               UUID chatId,
                                               UUID messageId,
                                               MultipartFile file) throws IOException {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        GroupChatFileMessage message = groupChatFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if( file == null || file.isEmpty() ){
            throw new MessageIsEmptyException();
        }

        if (!editor.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        new File(message.getFilePath()).delete();

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

        file.transferTo(new File(filePath));
        message.setFilePath(filePath);
        groupChatFileMessageRepository.save(message);

        return MessageRetrievalDto.builder()
                .messageId(messageId)
                .file(filePath)
                .nickname(editor.getNickname())
                .text(null)
                .build();


    }

    public MessageRetrievalDto editDescribedFileMessage(User editor,
                                                        UUID chatId,
                                                        UUID messageId,
                                                        MultipartFile file,
                                                        String text) throws IOException {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        GroupChatDescribedFileMessage message = groupChatDescribedFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if( StringUtils.isEmpty(text) && (file == null || file.isEmpty())){
            throw new MessageIsEmptyException();
        }

        if (!editor.equals(message.getAuthor())) {
            throw new MessageAuthorityException();
        }

        if (file != null && !file.isEmpty()) {

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "." + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;

            if (message.getFilePath().equals(filePath) && message.getDescription().equals(text)) {
                throw new MessageIsEqualException();
            }

            Files.delete(Paths.get(message.getFilePath()));
            file.transferTo(new File(filePath));
            message.setFilePath(filePath);
        }

        message.setDescription(StringUtils.defaultIfEmpty(text, message.getDescription()));
        groupChatDescribedFileMessageRepository.save(message);

        return MessageRetrievalDto.builder()
                .messageId(messageId)
                .file(message.getFilePath())
                .nickname(editor.getNickname())
                .text(text)
                .build();
    }

    public boolean deleteTextMessage(User requester, UUID chatId, UUID messageId) {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        final GroupChatTextMessage message = groupChatTextMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (requester.equals(message.getAuthor()) || !groupChatRepository.isAdminInGroupChat(chatId, requester.getId())) {
            throw new GroupChatPrivilegesException();
        }

        groupChatTextMessageRepository.delete(message);
        return true;
    }

    public boolean deleteFileMessage(User requester, UUID chatId, UUID messageId) throws IOException {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        final GroupChatFileMessage message = groupChatFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (requester.equals(message.getAuthor()) || !groupChatRepository.isAdminInGroupChat(chatId, requester.getId())) {
            throw new GroupChatPrivilegesException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        groupChatFileMessageRepository.delete(message);

        Files.delete(Paths.get(message.getFilePath()));

        return true;
    }

    public boolean deleteDescribedFileMessage(User requester, UUID chatId, UUID messageId) throws IOException {

        groupChatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);

        final GroupChatDescribedFileMessage message = groupChatDescribedFileMessageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (requester.equals(message.getAuthor()) || !groupChatRepository.isAdminInGroupChat(chatId, requester.getId())) {
            throw new GroupChatPrivilegesException();
        }

        Files.delete(Paths.get(message.getFilePath()));

        groupChatDescribedFileMessageRepository.delete(message);

        return true;
    }

}
