package com.alpha.voice.utils;

import com.alpha.voice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ValidateUser {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Integer getUserId(User user) {
        String sql = "SELECT id  FROM Users WHERE username = ? and password = ?";
        Integer idUser = -1;
        try {
            idUser = this.jdbcTemplate.queryForObject(sql, new String[]{user.getUsername(), user.getPassword()},
                    Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idUser;
    }
    public User getUserById(Integer id) {
        try {
            String sql = "SELECT username, fullname FROM Users WHERE id = ?";
            User user = this.jdbcTemplate.queryForObject(sql, new Integer[]{id}, new BeanPropertyRowMapper<>(User.class));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public User getUserByUsername(String username) {
        String sql = "SELECT username, fullname FROM Users WHERE username = ?";
        User user = this.jdbcTemplate.queryForObject(sql, new String[]{username}, new BeanPropertyRowMapper<>(User.class));
        return user;
    }
    public Integer validateUuid(String sessionUserId) {
        String sql = "SELECT user_id  FROM Session WHERE session_id = ?";
        Integer idUser = -1;
        try {
            idUser = this.jdbcTemplate.queryForObject(sql, new String[]{sessionUserId}, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return idUser;
        }
        return idUser;
    }
}
