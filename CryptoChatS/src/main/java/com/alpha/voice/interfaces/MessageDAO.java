package com.alpha.voice.interfaces;

import com.alpha.voice.model.Message;
import com.alpha.voice.utils.ValidateUser;

import java.util.List;

public interface MessageDAO {
    public List<Message> getFriendMessages(String user1SessionId, String user2Username);
    public Integer store(Message message);
}
