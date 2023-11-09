package com.eric.fileshare.service;

import com.eric.fileshare.beans.File;
import org.springframework.dao.DataAccessException;

public interface IFileService {

    void upload(File file) throws DataAccessException;

    File download(String hash) throws DataAccessException;

    void deleteFileById(int id) throws DataAccessException;
}
