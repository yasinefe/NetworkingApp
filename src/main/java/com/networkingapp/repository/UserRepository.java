package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.exception.UserNotFoundException;


public interface UserRepository {

    User getUser(String username) throws UserNotFoundException;

    void persistUser(User user);

}
