package com.networkingapp.controller;

import com.networkingapp.Application;
import com.networkingapp.domain.User;
import com.networkingapp.domain.UserMessage;
import com.networkingapp.exception.UserNotFoundException;
import com.networkingapp.service.UserService;
import com.networkingapp.test.util.ObjectMother;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user = ObjectMother.createAUser();
    private User followingUser = ObjectMother.createAUser();
    private UserMessage message = ObjectMother.createAMessage(user);
    private UserMessage anotherMessage = ObjectMother.createAMessage(user);

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldPostAMessageForGivenUser() throws Exception {
        mockMvc.perform(post("/user/{user}/message", user.getName())
                .content(jsonMessage(message.getMessage()))
                .contentType(contentType))
                .andExpect(status().isCreated());

        verify(userService).post(user.getName(), message.getMessage());
    }

    @Test
    public void shouldReturnBadRequestIfMessageIsEmpty() throws Exception {
        mockMvc.perform(post("/user/{user}/message", user.getName())
                .content(jsonMessage(""))
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verify(userService, never()).post(user.getName(), message.getMessage());
    }

    @Test
    public void shouldReturnBadRequestIfMessageHasMoreThan140Chars() throws Exception {
        mockMvc.perform(post("/user/{user}/message", user.getName())
                .content(jsonMessage(StringUtils.repeat("1234567890", 15)))
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verify(userService, never()).post(user.getName(), message.getMessage());
    }

    @Test
    public void shouldUserFollowGivenFollowingUser() throws Exception {
        mockMvc.perform(put("/user/{user}/follow/{followingUser}", user.getName(), followingUser.getName())
                .contentType(contentType))
                .andExpect(status().isAccepted());

        verify(userService).follow(user.getName(), followingUser.getName());
    }

    @Test
    public void shouldReturnNotFoundWhenUserNotFoundExceptionIsThrown() throws Exception {
        doThrow(new UserNotFoundException(user.getName())).when(userService)
                .follow(user.getName(), followingUser.getName());
        mockMvc.perform(put("/user/{user}/follow/{followingUser}", user.getName(), followingUser.getName())
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnMessagesOfGivenUserWhenWallIsCalled() throws Exception {
        Mockito.when(userService.getWall(user.getName())).thenReturn(Arrays.asList(message, anotherMessage));

        mockMvc.perform(get("/user/{user}/wall", user.getName())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].userName", is(user.getName())))
                .andExpect(jsonPath("$[0].message", is(message.getMessage())))
                .andExpect(jsonPath("$[0].createdAt", is(message.getCreatedAt())))
                .andExpect(jsonPath("$[1].userName", is(user.getName())))
                .andExpect(jsonPath("$[1].message", is(anotherMessage.getMessage())))
                .andExpect(jsonPath("$[1].createdAt", is(anotherMessage.getCreatedAt())));

    }

    @Test
    public void shouldReturnAllMessagesOfGivenUserWhenTimelineIsCalled() throws Exception {
        Mockito.when(userService.getTimeline(user.getName())).thenReturn(Arrays.asList(message, anotherMessage));

        mockMvc.perform(get("/user/{user}/timeline", user.getName())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].userName", is(user.getName())))
                .andExpect(jsonPath("$[0].message", is(message.getMessage())))
                .andExpect(jsonPath("$[0].createdAt", is(message.getCreatedAt())))
                .andExpect(jsonPath("$[1].userName", is(user.getName())))
                .andExpect(jsonPath("$[1].message", is(anotherMessage.getMessage())))
                .andExpect(jsonPath("$[1].createdAt", is(anotherMessage.getCreatedAt())));

    }

    private byte[] jsonMessage(String message) {
        return ("{\"message\":\"" + message + "\"}").getBytes();
    }

}