package com.eric.fileshare.dao;


import com.eric.fileshare.beans.File;
import org.springframework.dao.DataAccessException;

public interface IFileDAO {

    void insertFile(File file) throws DataAccessException;

    File findFileByHash(String hash) throws DataAccessException;

    void deleteFileById(int id) throws DataAccessException;
}
