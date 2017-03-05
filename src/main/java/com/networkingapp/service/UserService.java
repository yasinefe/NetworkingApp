package com.networkingapp.service;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.exception.UserNotFoundException;
import com.networkingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.networkingapp.domain.User.createUser;

@Service
public class UserService {

    private UserRepository userRepository;
    private MessageService messageService;

    @Autowired
    public UserService(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    public void post(String username, String message) {
        messageService.createMessage(getUserOrCreateOne(username), message);
    }

    private User getUserOrCreateOne(String username) {
        try {
            return userRepository.getUser(username);
        } catch (UserNotFoundException e) {
            User user = createUser(username);
            userRepository.persistUser(user);
            return user;
        }
    }

    public List<UserMessage> getWall(String username) throws UserNotFoundException {
        return messageService.getMessages(userRepository.getUser(username));
    }

    public void follow(String username, String followingUsername) throws UserNotFoundException {
        User user = userRepository.getUser(username);
        user.follow(userRepository.getUser(followingUsername));
        userRepository.persistUser(user);
    }

    // The test document says : A user should be able to see a list of the messages posted by all the people they follow,
    // in reverse chronological order.
    // I would ask that if the timeline will include the messages from given user. Usually,
    // in social apps they includes the messages from given user
    // I assumed timeline should include messages from the user and messages from the following users.
    public List<UserMessage> getTimeline(String username) throws UserNotFoundException {
        User user = userRepository.getUser(username);
        List<User> users = new ArrayList<>(user.getFollowingUsers());
        users.add(user);
        return messageService.getMessages(users);
    }

}
