package com.networkingapp.domain;

import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class User {

    private String name;
    private List<User> followingUsers = new ArrayList<>();

    private User(String name) {
        this.name = name;
    }

    public static User createUser(String name) {
        return new User(name);
    }

    public String getName() {
        return name;
    }

    public List<User> getFollowingUsers() {
        return followingUsers;
    }

    public void follow(User user) {
        followingUsers.add(user);
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
}
