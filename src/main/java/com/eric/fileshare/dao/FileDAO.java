package com.eric.fileshare.dao;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.mapper.FileMapper;
import com.eric.fileshare.util.EncryptionUtil;
import com.eric.fileshare.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FileDAO implements IFileDAO{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FileDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertFile(File file) {
        String sql = """
                INSERT INTO file VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, file.getFilename(), file.getExtension(), file.getUploaderID(), file.getUploaderIP(), file.getContent(), file.getExpireAt(), file.getUploadAt(), file.getHash());
    }

    @Override
    public File findFileByHash(String hash) {
        String sql = """
                SELECT * FROM file WHERE hash = ?
                """;
        File file = jdbcTemplate.queryForObject(sql, new FileMapper(), hash);
        return file;
    }
}
