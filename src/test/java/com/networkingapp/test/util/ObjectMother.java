package com.networkingapp.test.util;

import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.util.Clock;

import java.util.Date;

import static com.networkingapp.domain.User.createUser;
import static com.networkingapp.domain.UserMessage.createUserMessage;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectMother {

    private static long index = 0;
    private static Date now = new Date();

    public static User createAUser() {
        return createUser("username_" + increase());
    }

    public static UserMessage createAMessage(User user) {
        return createUserMessage(user.getName(), "message_" + increase(), now);
    }

    public static UserMessage createAMessageInFuture(User user, int minutesLater) {
        Date date = new Date(now.getTime() + MINUTES.toMillis(minutesLater));
        return createUserMessage(user.getName(), "message_" + increase(), date);
    }

    public static Clock createClock() {
        Clock clock = mock(Clock.class);
        when(clock.now()).thenReturn(now);
        return clock;
    }

    private static synchronized long increase() {
        return index++;
    }

}
