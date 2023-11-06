package com.eric.fileshare.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VisitorDAO implements IVisitorDAO{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public VisitorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getOccupiedSpaceByIp(String ip) {
        String sql = """
                    SELECT SUM(filesize) FROM file WHERE uploader_ip = ?
                """;
        Long size = jdbcTemplate.queryForObject(sql, Long.class, ip);
        return size;
    }
}
