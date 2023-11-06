package com.eric.fileshare.service;

import com.eric.fileshare.beans.File;
import com.eric.fileshare.dao.IFileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService implements IFileService{
    private IFileDAO fileDAO;

    public FileService(){}

    @Autowired
    public FileService(IFileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    @Override
    public void upload(File file) {
        fileDAO.insertFile(file);
    }

    @Override
    public File download(String hash) {
        return fileDAO.findFileByHash(hash);
    }
}
