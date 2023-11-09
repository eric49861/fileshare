package com.eric.fileshare.dao;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public void insertFile(File file) throws DataAccessException {
        String sql = """
                INSERT INTO file VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql, file.getUploaderIP(), file.getFilename(), file.getExtension(), file.getContent(), file.getExpireAt(), file.getUploadAt(), file.getHash(), file.getFilesize());
        }catch (DataAccessException e) {
            throw e;
        }
    }

    @Override
    public File findFileByHash(String hash) throws DataAccessException{
        String sql = """
                SELECT * FROM file WHERE hash = ? LIMIT 1
                """;
        File file = null;
        try {
            file = jdbcTemplate.queryForObject(sql, new FileMapper(), hash);
        }catch(DataAccessException e) {
            throw e;
        }
        return file;
    }

    @Override
    public void deleteFileById(int id) throws DataAccessException{
        String sql = """
                DELETE FROM file WHERE id = ?
                """;
        try {
            jdbcTemplate.update(sql, id);
        }catch(DataAccessException e) {
            throw e;
        }
    }
}
