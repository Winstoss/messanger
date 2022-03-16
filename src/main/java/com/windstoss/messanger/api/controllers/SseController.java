package com.windstoss.messanger.api.controllers;

import com.windstoss.messanger.api.dto.Message.DeleteMessageRetrievalDto;
import com.windstoss.messanger.api.dto.Message.MessageRetrievalDto;
import com.windstoss.messanger.domain.User;
import com.windstoss.messanger.services.GroupChatService;
import com.windstoss.messanger.services.PrivateChatService;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin
@RestController
@RequestMapping("/chats")
public class SseController {

    private final PrivateChatService privateChatService;

    private final GroupChatService groupChatService;

    public SseController(PrivateChatService privateChatService,
                         GroupChatService groupChatService) {
        this.privateChatService = privateChatService;
        this.groupChatService = groupChatService;
    }

    private final ConcurrentHashMap<UUID, CopyOnWriteArrayList<SseEmitter>> privateChatSubscriptions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CopyOnWriteArrayList<SseEmitter>> groupChatSubscriptions = new ConcurrentHashMap<>();

    @RequestMapping("/private/{userId}/sse-stream")
    public SseEmitter handlePrivateMessage(@PathVariable("userId") UUID userId,
                                           UsernamePasswordAuthenticationToken principal) {

        UUID chatId = privateChatService.getPrivateChat((User) principal.getPrincipal(), userId).getId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        privateChatSubscriptions.computeIfAbsent( chatId, key -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onTimeout(() ->privateRemover(chatId, emitter));
        emitter.onCompletion(() ->privateRemover(chatId, emitter));
        emitter.onError(Throwable::printStackTrace);

        return emitter;
    }


    @Async
    @EventListener
    public void handlePrivateMessage(MessageRetrievalDto message) {
        var listsToDelete = new CopyOnWriteArrayList<SseEmitter>();
        privateChatSubscriptions.get(message.getChatId()).forEach(
                sseEmitter -> {
                    try {
                        sseEmitter.send(message, MediaType.APPLICATION_JSON);
                    } catch (IOException e) {
                        listsToDelete.add(sseEmitter);
                    }
                }
        );
        privateChatSubscriptions.get(message.getChatId()).removeAll(listsToDelete);
    }

    @Async
    @EventListener
    public void deletePrivateMessage(DeleteMessageRetrievalDto deletedMessage) {
        var listsToDelete = new CopyOnWriteArrayList<SseEmitter>();
        privateChatSubscriptions.get(deletedMessage.getChatId()).forEach(
                sseEmitter -> {
                    try {
                        sseEmitter.send(deletedMessage, MediaType.APPLICATION_JSON);
                    } catch (IOException e) {
                        listsToDelete.add(sseEmitter);
                    }
                }
        );
        privateChatSubscriptions.get(deletedMessage.getChatId()).removeAll(listsToDelete);
    }

    private void privateRemover(UUID chatId, SseEmitter emitter) {
        privateChatSubscriptions.computeIfPresent(chatId, (key, array) -> {
            array.remove(emitter);
            return array;
        });
    }
}
