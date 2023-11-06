package com.eric.fileshare;

import com.eric.fileshare.beans.User;
import com.eric.fileshare.config.SpringConfig;
import com.eric.fileshare.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.time.Duration;


@SpringJUnitConfig(SpringConfig.class)
public class TestRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testConnection() {
        redisTemplate.opsForValue().set("name", "eric", Duration.ofMinutes(1));

        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name.toString());
    }

    @Test
    public void testJdbc() {
        String sql = """
                SELECT * FROM user WHERE email = ?
                """;
        User user = jdbcTemplate.queryForObject(sql, new UserMapper(), "1585416826@qq.com");
        System.out.println(user);
    }


}
