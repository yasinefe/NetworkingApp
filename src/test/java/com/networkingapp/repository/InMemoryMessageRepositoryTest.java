package com.networkingapp.repository;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.networkingapp.test.util.ObjectMother.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InMemoryMessageRepositoryTest {

    private MessageRepository messageRepository = new InMemoryMessageRepository();

    private User user = createAUser();
    private User anotherUser = createAUser();
    private UserMessage message = createAMessage(user);
    private UserMessage anotherMessage = createAMessageInFuture(anotherUser, 1);

    @Test
    public void getMessagesShouldReturnEmptyListIfThereIsNoMessageForGivenUser() throws Exception {
        // When
        List<UserMessage> actualMessages = messageRepository.getMessages(user);

        // Then
        assertThat(actualMessages, equalTo(emptyList()));
    }

    @Test
    public void saveMessageShouldStoreMessageInMemory() throws Exception {
        // When
        messageRepository.persistMessage(message);

        // Then
        assertThat(messageRepository.getMessages(user), equalTo(singletonList(message)));
    }

    @Test
    public void getMessagesShouldReturnOnlyMessagesOfGivenUser() throws Exception {
        // Given
        messageRepository.persistMessage(message);
        messageRepository.persistMessage(anotherMessage);

        // When
        List<UserMessage> actualMessages = messageRepository.getMessages(user);

        // Then
        assertThat(actualMessages, equalTo(singletonList(message)));
    }

    @Test
    public void getMessagesShouldReturnOnlyMessagesOfGivenUserInReverseChronologicalOrder() throws Exception {
        // Given
        User user = createAUser();
        UserMessage message1 = createAMessageInFuture(user, 1);
        UserMessage message2 = createAMessageInFuture(user, 2);
        UserMessage message3 = createAMessageInFuture(user, 3);

        // When
        messageRepository.persistMessage(message1);
        messageRepository.persistMessage(message2);
        messageRepository.persistMessage(message3);

        // Then
        List<UserMessage> actualMessages = messageRepository.getMessages(user);

        assertThat(actualMessages.size(), equalTo(3));
        assertThat(actualMessages.get(0), equalTo(message3));
        assertThat(actualMessages.get(1), equalTo(message2));
        assertThat(actualMessages.get(2), equalTo(message1));
    }

    @Test
    public void getMessagesShouldReturnOnlyMessagesOfGivenUsers() throws Exception {
        // Given
        messageRepository.persistMessage(message);
        messageRepository.persistMessage(anotherMessage);

        // When
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(anotherUser);

        List<UserMessage> actualMessages = messageRepository.getMessages(users);

        // Then
        assertThat(actualMessages.size(), equalTo(2));
        assertThat(actualMessages.get(0), equalTo(anotherMessage));
        assertThat(actualMessages.get(1), equalTo(message));
    }

    @Test
    public void getMessagesShouldReturnOnlyMessagesOfGivenUsersWhenDifferentUsersHaveDifferentMessagesInReverseChronologicalOrder()
            throws Exception {
        // Given
        User user1 = createAUser();
        UserMessage message1 = createAMessageInFuture(user1, 1);
        UserMessage message2 = createAMessageInFuture(user1, 2);

        User user2 = createAUser();
        UserMessage message3 = createAMessageInFuture(user2, 3);
        UserMessage message4 = createAMessageInFuture(user2, 4);

        User user3 = createAUser();
        UserMessage message5 = createAMessageInFuture(user3, 5);
        UserMessage message6 = createAMessageInFuture(user3, 6);

        messageRepository.persistMessage(message1);
        messageRepository.persistMessage(message2);
        messageRepository.persistMessage(message3);
        messageRepository.persistMessage(message4);
        messageRepository.persistMessage(message5);
        messageRepository.persistMessage(message6);

        // When
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<UserMessage> actualMessages = messageRepository.getMessages(users);

        // Then
        assertThat(actualMessages.size(), equalTo(4));
        assertThat(actualMessages.get(0), equalTo(message4));
        assertThat(actualMessages.get(1), equalTo(message3));
        assertThat(actualMessages.get(2), equalTo(message2));
        assertThat(actualMessages.get(3), equalTo(message1));
    }
}
