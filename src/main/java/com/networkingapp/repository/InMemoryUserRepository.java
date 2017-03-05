package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> users = new HashMap<>();

    @Override
    public User getUser(String username) throws UserNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }

    @Override
    public void persistUser(User user) {
        users.put(user.getName(), user);
    }


}
