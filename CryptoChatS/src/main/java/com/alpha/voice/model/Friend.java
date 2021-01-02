package com.alpha.voice.model;

import java.util.Dictionary;
import java.util.List;

public class Friend {
    private String username;
    private String fullName;
    private String lastMessage;

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return String.format(
                "{ \"username\": \"%s\", \"fullName\": \"%s\", \"lastMessage\": \"%s\" }",
                this.username, this.fullName, this.lastMessage);
    }
}
