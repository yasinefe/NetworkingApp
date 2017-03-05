package com.networkingapp.service;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.repository.MessageRepository;
import com.networkingapp.test.util.ObjectMother;
import org.junit.Test;

import java.util.List;

import static com.networkingapp.test.util.ObjectMother.createAUser;
import static com.networkingapp.test.util.ObjectMother.createClock;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    private MessageRepository mockMessageRepository = mock(MessageRepository.class);
    private MessageService messageService = new MessageService(createClock(), mockMessageRepository);

    private User user = createAUser();
    private UserMessage message = ObjectMother.createAMessage(user);

    @Test
    public void createMessageShouldCreateAMessageAndPersistIt() throws Exception {
        // When
        messageService.createMessage(user, message.getMessage());

        // Then
        verify(mockMessageRepository).persistMessage(message);
    }

    @Test
    public void getMessagesShouldReturnMessagesFromRepository() throws Exception {
        // Given
        List<UserMessage> expectedMessages = singletonList(message);
        when(mockMessageRepository.getMessages(user)).thenReturn(expectedMessages);

        // When
        List<UserMessage> actualMessages = messageService.getMessages(user);

        // Then
        assertThat(actualMessages, equalTo(expectedMessages));
    }

    @Test
    public void getMessagesShouldReturnMessagesForGivenUsers() throws Exception {
        // Given
        List<UserMessage> expectedMessages = singletonList(message);
        List<User> users = asList(user, createAUser());
        when(mockMessageRepository.getMessages(users)).thenReturn(expectedMessages);

        // When
        List<UserMessage> actualMessages = messageService.getMessages(users);

        // Then
        assertThat(actualMessages, equalTo(expectedMessages));
    }

}
