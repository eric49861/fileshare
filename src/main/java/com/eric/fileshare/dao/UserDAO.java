package com.eric.fileshare.dao;

import com.eric.fileshare.beans.User;
import com.eric.fileshare.dto.SignupDTO;
import com.eric.fileshare.mapper.UserMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
public class UserDAO implements IUserDAO{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertUser(User user) {
        String sql = """
                INSERT INTO user VALUES(NULL, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getRegisterAt());
    }

    /*
    * 根据邮箱查询用户, 返回查询到的记录数
    * */
    @Override
    public User findUserByEmail(String email) {
        String sql = """
                SELECT * FROM user WHERE email = ?
                """;
        User user = jdbcTemplate.queryForObject(sql, new UserMapper(), email);
        return user;
    }

    @Override
    public int existEmail(String email) {
        String sql = """
                SELECT COUNT(*) FROM user WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count;
    }

    @Override
    public long getOccupiedSpaceByEmail(String email) {
        String sql = """
                SELECT SUM(*) FROM file WHERE email = ?
                """;
        Long size = jdbcTemplate.queryForObject(sql, Long.class, email);
        return size;
    }


}
