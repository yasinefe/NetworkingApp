package com.networkingapp.domain;

import org.apache.commons.lang3.builder.ToStringStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class UserMessage implements Comparable<UserMessage> {

    private String userName;
    private String message;
    private Date createdAt;

    private UserMessage(String userName, String message, Date createdAt) {
        this.userName = userName;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static UserMessage createUserMessage(String userName, String message, Date createdAt) {
        return new UserMessage(userName, message, createdAt);
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }

    public String getCreatedAt() {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(createdAt);
    }

    @Override
    public String toString() {
        return reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public int compareTo(UserMessage userMessage) {
        return this.getCreatedAt().compareTo(userMessage.getCreatedAt());
    }
}
