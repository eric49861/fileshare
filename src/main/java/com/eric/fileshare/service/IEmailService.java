package com.eric.fileshare.service;

import com.eric.fileshare.exceptions.upload.UploadException;
import org.apache.commons.mail.EmailException;

import java.util.concurrent.ExecutionException;

public interface IEmailService {

    void sendCode(String to) throws EmailException, ExecutionException;

    void sendLink(String link, String to) throws EmailException;

    void checkCode(String code, String email) throws UploadException, ExecutionException;
}
