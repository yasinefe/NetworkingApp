package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

@Repository
public class InMemoryMessageRepository implements MessageRepository {

    private List<UserMessage> messages = new ArrayList<>();

    @Override
    public void persistMessage(UserMessage message) {
        messages.add(message);
    }

    @Override
    public List<UserMessage> getMessages(User user) {
        return messages.stream()
                .filter(userMessage -> userMessage.getUserName().equals(user.getName()))
                .sorted(reverseOrder())
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMessage> getMessages(List<User> users) {
        return messages.stream()
                .filter(userMessage -> users.stream()
                        .filter(user -> userMessage.getUserName().equals(user.getName()))
                        .findFirst()
                        .isPresent())
                .sorted(reverseOrder())
                .collect(Collectors.toList());
    }

}
