package com.eric.fileshare;

import com.eric.fileshare.config.SpringConfig;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.concurrent.ExecutionException;

@SpringJUnitConfig(SpringConfig.class)
public class TestCache {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    @Qualifier(value = "codeCache")
    private LoadingCache<String, String> cache;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Test
    public void testCache() throws InterruptedException {

        logger.info("Hello World");

        cache.put("key", "123456");
        Thread.sleep(3000);
        try {
            cache.put("key", "123");
            Thread.sleep(3000);
            String key = cache.get("key");
            if(key == null) {
                System.out.println("key is null");
            }else{
                System.out.println("key = " + key);
            }
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testJDBC() {
        Integer integer = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        System.out.println(integer);
    }


}
