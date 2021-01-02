package com.alpha.voice.services;

import com.alpha.voice.interfaces.UserDAO;
import com.alpha.voice.model.Friend;
import com.alpha.voice.model.Message;
import com.alpha.voice.model.User;
import com.alpha.voice.utils.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserDAOService extends ValidateUser implements UserDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String addUser(User user) {
        try {
            this.jdbcTemplate.update("INSERT INTO Users(username, password, fullname, mail) VALUES(?, ?, ?, ?)",
                    user.getUsername(), user.getPassword(), user.getFullName(), user.getMail());
            return "Saved";
        } catch (Exception e) {
            e.printStackTrace();
            return "Not saved";
        }
    }


    @Override
    public String addUserSession(User user, String session_id) {
        Integer idUser = getUserId(user);
        String sql = "REPLACE INTO Session(user_id, session_id, created_at) VALUES(?, ?, ?)";
        this.jdbcTemplate.update(sql, idUser, session_id, new Timestamp(new Date().getTime()));
        return "Saved";
    }

    @Override
    public User getRegistredUser(User user) {
        User conUser = null;
        try {
            String sql = "SELECT username, password, fullname, mail  FROM Users WHERE username = ? and password = ?";
            conUser = this.jdbcTemplate.queryForObject(sql, new String[]{user.getUsername(), user.getPassword()},
                    new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conUser;
    }

    @Override
    public List<Integer> getFriendIds(Integer userId) {
        List<Integer> friendIds = null;
        if(userId != -1){
            String sql = "SELECT u2_id from Friends WHERE u1_id=?";
            friendIds = this.jdbcTemplate.query(sql, new Integer[]{userId}, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    Integer id = resultSet.getInt("u2_id");
                    return id;
                }
            });
        }
        return friendIds;
    }

    @Override
    public List<Friend> getFriends(String sessionUserId) {
        List<Friend> friends = new LinkedList<>();
        Integer userId = validateUuid(sessionUserId);
        List<Integer> friendIds = getFriendIds(userId);
        User user = getUserById(userId);
        if(friendIds != null) {
            for(Integer id : friendIds) {
                String sql = "SELECT username, fullname FROM Users WHERE  id=?;";
                Friend friend = this.jdbcTemplate.queryForObject(sql, new Integer[]{id}, new BeanPropertyRowMapper<>(Friend.class));
                try {
                    sql = "SELECT message FROM Messages WHERE `from`= ? and `to`= ? and type_message != 'image' ORDER BY id DESC limit 1";
                    String lastMessage = this.jdbcTemplate.queryForObject(sql, new String[]{friend.getUsername(), user.getUsername()}, String.class);
                    friend.setLastMessage(lastMessage);
                } catch (Exception e) {
                    System.out.printf("Nu exista un ultim mesaj la acest prieten(%s)%n", friend.getFullName());
//                    e.printStackTrace();
                    friend.setLastMessage("");
                }
                friends.add(friend);
            }
        }
        return friends;
    }

    @Override
    public String generatePairKey(String sessionUserId) {
        try {
            Integer userId = validateUuid(sessionUserId);
            if(userId != -1) {
                User user = getUserById(userId);
                if(user != null) {
                    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                    kpg.initialize(1024);
                    KeyPair kp = kpg.generateKeyPair();
                    Key pub = kp.getPublic();
                    Key pvt = kp.getPrivate();
                    String pubEncBase64 = Base64.getEncoder().encodeToString(pub.getEncoded());
                    String pvtEncBase64 = Base64.getEncoder().encodeToString(pvt.getEncoded());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    this.jdbcTemplate.update("REPLACE INTO EPairKeyServer(username, fullName, keyPb, keyPv, formatKeyPb, formatKeyPv, createdAt) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)", user.getUsername(), user.getFullName(), pubEncBase64, pvtEncBase64, pub.getFormat(), pvt.getFormat(), timestamp.getTime());
                    return Base64.getEncoder().encodeToString(pub.getEncoded());
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Integer updatePublicClientKey(String sessionUserId, String pbKey) {
        try {
            Integer userId = validateUuid(sessionUserId);
            if(userId != -1) {
                User user = getUserById(userId);
                if(user != null) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    this.jdbcTemplate.update("REPLACE INTO EPairKeyClient(username, fullName, keyPb, createdAt) " +
                            "VALUES(?, ?, ?, ?)", user.getUsername(), user.getFullName(), pbKey, timestamp.getTime());
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getPrivateKeyServer(String username) {
        try {
            String sql = "SELECT keyPv FROM EPairKeyServer WHERE username=?";
            return this.jdbcTemplate.queryForObject(sql, new String[]{username}, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPublicKeyClient(String username) {
        try {
            String sql = "SELECT keyPb FROM EPairKeyClient WHERE username=?";
            return this.jdbcTemplate.queryForObject(sql, new String[]{username}, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer validateUser(User user) {
        return this.getUserId(user);
    }

}
