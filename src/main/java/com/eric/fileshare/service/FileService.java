package com.eric.fileshare.service;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.dao.IFileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class FileService implements IFileService {
    private IFileDAO fileDAO;

    public FileService(){}

    @Autowired
    public FileService(IFileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    @Override
    public void upload(File file) throws DataAccessException{
        try {
            fileDAO.insertFile(file);
        }catch (DataAccessException e) {
            throw e;
        }
    }

    @Override
    public File download(String hash) throws DataAccessException {
        File file = null;
        try {
            file = fileDAO.findFileByHash(hash);
        }catch (DataAccessException e) {
            throw e;
        }
        return file;
    }

    @Override
    public void deleteFileById(int id) throws DataAccessException{
        try {
            fileDAO.deleteFileById(id);
        }catch (DataAccessException e) {
            throw e;
        }
    }
}
