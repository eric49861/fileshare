package com.eric.fileshare.mapper;

import com.eric.fileshare.beans.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setId(rs.getObject("id", Integer.class));
        u.setEmail(rs.getObject("email", String.class));
        u.setName(rs.getObject("username", String.class));
        u.setPassword(rs.getObject("password", String.class));
        u.setRegisterAt(rs.getObject("registerAt", Timestamp.class));
        return u;
    }
}
