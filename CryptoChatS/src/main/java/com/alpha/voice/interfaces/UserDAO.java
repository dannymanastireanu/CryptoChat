package com.alpha.voice.interfaces;

import com.alpha.voice.model.Friend;
import com.alpha.voice.model.User;
import com.alpha.voice.utils.ValidateUser;

import java.util.List;

public interface UserDAO {

    public String addUser(User user);
    public String addUserSession(User user, String session_id);
    public User getRegistredUser(User user);
    public List<Integer> getFriendIds(Integer userId);
    public List<Friend> getFriends(String sessionUserId);
    public User getUserById(Integer id);
    public String generatePairKey(String sessionUserId);
    public Integer updatePublicClientKey(String sessionUserId, String pbKey);
    public String getPrivateKeyServer(String username);
    public String getPublicKeyClient(String username);

    public Integer validateUser(User user);
}
