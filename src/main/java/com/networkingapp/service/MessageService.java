package com.networkingapp.service;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.repository.MessageRepository;
import com.networkingapp.util.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.networkingapp.domain.UserMessage.createUserMessage;

@Service
public class MessageService {

    private Clock clock;
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(Clock clock, MessageRepository messageRepository) {
        this.clock = clock;
        this.messageRepository = messageRepository;
    }

    public void createMessage(User user, String message) {
        messageRepository.persistMessage(createUserMessage(user.getName(), message, clock.now()));
    }

    public List<UserMessage> getMessages(User user) {
        return messageRepository.getMessages(user);
    }

    public List<UserMessage> getMessages(List<User> users) {
        return messageRepository.getMessages(users);
    }

}
