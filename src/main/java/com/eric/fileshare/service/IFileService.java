package com.eric.fileshare.service;

import com.eric.fileshare.beans.File;

public interface IFileService {

    void upload(File file);

    File download(String hash);
}
