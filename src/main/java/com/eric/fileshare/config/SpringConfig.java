package com.eric.fileshare.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class SpringConfig {
    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public DruidDataSource getDruidDatsSource(){
        InputStream inputStream = null;
        Properties properties = new Properties();
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.configFromPropety(properties);
        return druidDataSource;
    }

    /*
    * 引入guava cache缓存作为邮箱验证码的本地缓存
    * */
    @Bean(value = "codeCache")
    public LoadingCache<String, String> getCodeLoadingCache() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return "";
                    }
                });
        return cache;
    }

//    @Bean
//    public RedisConnectionFactory getRedisConnectionFactory() {
//        // 加载配置文件
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("redis.properties");
//        Properties properties = new Properties();
//        try {
//            properties.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if(inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        // 配置Redis的连接
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setDatabase(Integer.parseInt(properties.get("redis.index").toString()));
//        redisConfig.setHostName(properties.getProperty("redis.host"));
//        redisConfig.setPassword(properties.getProperty("redis.password"));
//        redisConfig.setPort(Integer.parseInt(properties.get("redis.port").toString()));
//
//        // todo: 配置连接池的属性
//        // ...
//
//        return new JedisConnectionFactory(redisConfig);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        return redisTemplate;
//    }

    @Bean
    public MultipartResolver getStandardServletMultipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }
}
