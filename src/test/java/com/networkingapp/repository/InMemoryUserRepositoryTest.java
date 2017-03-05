package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static com.networkingapp.test.util.ObjectMother.createAUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InMemoryUserRepositoryTest {

    private UserRepository userRepository;

    private User user = createAUser();
    private User anotherUser = createAUser();

    @Before
    public void setup() throws Exception {
        userRepository = new InMemoryUserRepository();
    }

    @Test(expected = UserNotFoundException.class)
    public void getUserWithNameShouldThrowUserNotFoundException() throws Exception {
        // When
        userRepository.getUser(user.getName());
    }

    @Test
    public void getUserWithNameShouldReturnCorrectUser() throws Exception {
        // When
        userRepository.persistUser(user);
        userRepository.persistUser(anotherUser);

        // Then
        assertThat(userRepository.getUser(user.getName()), equalTo(user));
        assertThat(userRepository.getUser(anotherUser.getName()), equalTo(anotherUser));
    }

    @Test
    public void saveUserShouldStoreUserInMemory() throws Exception {
        // When
        userRepository.persistUser(user);

        // Then
        assertThat(userRepository.getUser(user.getName()), equalTo(user));
    }

}
