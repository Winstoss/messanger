package com.windstoss.messanger.services;

import com.windstoss.messanger.repositories.GroupChatFileMessageRepository;
import com.windstoss.messanger.repositories.GroupChatTextMessageRepository;
import com.windstoss.messanger.repositories.PrivateChatFileMessageRepository;
import com.windstoss.messanger.repositories.PrivateChatTextMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class MessageService {

    private final GroupChatFileMessageRepository groupChatFileMessageRepository;

    private final GroupChatTextMessageRepository groupChatTextMessageRepository;

    private final PrivateChatFileMessageRepository privateChatFileMessageRepository;

    private final PrivateChatTextMessageRepository privateChatTextMessageRepository;

    public MessageService(GroupChatFileMessageRepository groupChatFileMessageRepository,
                          GroupChatTextMessageRepository groupChatTextMessageRepository,
                          PrivateChatTextMessageRepository privateChatTextMessageRepository,
                          PrivateChatFileMessageRepository privateChatFileMessageRepository){
        this.groupChatFileMessageRepository = Objects.requireNonNull(groupChatFileMessageRepository);
        this.groupChatTextMessageRepository = Objects.requireNonNull(groupChatTextMessageRepository);
        this.privateChatTextMessageRepository = Objects.requireNonNull(privateChatTextMessageRepository);
        this.privateChatFileMessageRepository = Objects.requireNonNull(privateChatFileMessageRepository);
    }

    public void sendMessage(){

    }

    public void deleteMessage(){

    }

    public void editMessage(){

    }


}
