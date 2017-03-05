package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;

import java.util.List;

public interface MessageRepository {

    void persistMessage(UserMessage message);

    List<UserMessage> getMessages(User user);

    List<UserMessage> getMessages(List<User> users);

}
