package com.networkingapp.service;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.exception.UserNotFoundException;
import com.networkingapp.repository.UserRepository;
import com.networkingapp.test.util.ObjectMother;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.networkingapp.test.util.ObjectMother.createAUser;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository mockUserRepository = mock(UserRepository.class);
    private MessageService mockMessageService = mock(MessageService.class);
    private UserService userService = new UserService(mockMessageService, mockUserRepository);

    private User user = createAUser();
    private User followingUser = createAUser();

    private UserMessage message = ObjectMother.createAMessage(user);

    @Before
    public void setup() throws Exception {
        when(mockUserRepository.getUser(user.getName())).thenReturn(user);
        when(mockUserRepository.getUser(followingUser.getName())).thenReturn(followingUser);
    }

    @Test
    public void postShouldCreateAMessageUsingMessageService() throws Exception {
        // When
        userService.post(user.getName(), message.getMessage());

        // Then
        verify(mockMessageService).createMessage(user, message.getMessage());
    }

    @Test
    public void postShouldCreateAUserFirstIfUserNotFound() throws Exception {
        // Given
        User anotherUser = createAUser();
        when(mockUserRepository.getUser(anotherUser.getName())).thenThrow(
                new UserNotFoundException(anotherUser.getName()));

        // When
        userService.post(anotherUser.getName(), message.getMessage());

        // Then
        verify(mockUserRepository).persistUser(anotherUser);
        verify(mockMessageService).createMessage(anotherUser, message.getMessage());
    }

    @Test
    public void getWallShouldReturnMessages() throws Exception {
        // Given
        List<UserMessage> expectedMessages = singletonList(message);
        when(mockMessageService.getMessages(user)).thenReturn(expectedMessages);

        // When
        List<UserMessage> actualMessages = userService.getWall(user.getName());

        // Then
        assertThat(actualMessages, equalTo(expectedMessages));
    }

    @Test
    public void followShouldAddFollowingUserAndSaveUser() throws Exception {
        // When
        userService.follow(user.getName(), followingUser.getName());

        // Then
        verify(mockUserRepository).persistUser(user);
        assertThat(user.getFollowingUsers(), equalTo(singletonList(followingUser)));
    }

    @Test
    public void getTimelineShouldReturnMessagesOnTheTimeline() throws Exception {
        // Given
        user.follow(followingUser);
        List<UserMessage> expectedMessages = singletonList(message);
        when(mockMessageService.getMessages(asList(followingUser, user))).thenReturn(expectedMessages);

        // When
        List<UserMessage> actualMessages = userService.getTimeline(user.getName());

        // Then
        assertThat(actualMessages, equalTo(expectedMessages));
    }

}
