package com.eric.fileshare.mapper;

import com.eric.fileshare.beans.File;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FileMapper implements RowMapper<File> {
    @Override
    public File mapRow(ResultSet rs, int rowNum) throws SQLException {
        if(rowNum == 0) {
            return null;
        }
        File f = new File();
        f.setId(rs.getObject("id", Integer.class));
        f.setFilename(rs.getObject("filename", String.class));
        f.setExtension(rs.getObject("extension", String.class));
        f.setUploaderIP(rs.getObject("uploader_ip", String.class));
        f.setContent(rs.getObject("content", byte[].class));
        f.setExpireAt(rs.getObject("expire_at", Timestamp.class));
        f.setUploadAt(rs.getObject("upload_at", Timestamp.class));
        f.setHash(rs.getObject("hash", String.class));
        f.setFilesize(rs.getObject("filesize", Long.class));
        return f;
    }
}
