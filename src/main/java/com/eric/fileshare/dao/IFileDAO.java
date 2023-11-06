package com.eric.fileshare.dao;


import com.eric.fileshare.beans.File;

public interface IFileDAO {

    void insertFile(File file);

    File findFileByHash(String hash);
}
