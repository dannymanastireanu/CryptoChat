package com.alpha.voice.services;

import com.alpha.voice.interfaces.MessageDAO;
import com.alpha.voice.model.Message;
import com.alpha.voice.model.User;
import com.alpha.voice.utils.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MessageDAOService extends ValidateUser implements MessageDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getUserId(User user) {
        String sql = "SELECT id  FROM Users WHERE username = ? and password = ?";
        Integer idUser = this.jdbcTemplate.queryForObject(sql, new String[]{user.getUsername(), user.getPassword()},
                Integer.class);
        return idUser;
    }

    @Override
    public List<Message> getFriendMessages(String user1SessionId, String user2Username) {
        List<Message> messages = null;
        Integer user1Id = validateUuid(user1SessionId);
        User user1 = getUserById(user1Id);
        String sql = "SELECT * FROM Messages WHERE (`from`=? and `to`=?) or (`to`=? and `from`=?)";
        try{
            messages = this.jdbcTemplate.query(sql, new String[]{user1.getUsername(), user2Username, user1.getUsername(), user2Username}, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet resultSet, int i) throws SQLException {
                    Message message = new Message();
                    message.setFrom(resultSet.getString("from"));
                    message.setTo(resultSet.getString("to"));
                    message.setBody(resultSet.getString("message"));
                    message.setTimestamp(resultSet.getLong("timestamp"));
                    message.setType(resultSet.getString("type_message"));
                    message.setCachePath(resultSet.getString("cache_path"));
                    return message;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public Integer store(Message message) {
        String sql = "INSERT INTO Messages(`from`, `to`, message, timestamp, type_message, cache_path) VALUES(?, ?, ?, ?, ?, ?)";
        try{
            Integer rowModified = this.jdbcTemplate.update(sql, message.getFrom(), message.getTo(), message.getBody(),
                    message.getTimestamp(), message.getType(), message.getCachePath());
            return rowModified;
        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


}
