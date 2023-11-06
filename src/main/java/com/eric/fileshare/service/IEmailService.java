package com.eric.fileshare.service;

import org.apache.commons.mail.EmailException;

public interface IEmailService {

    void sendCode(String to) throws EmailException;

    void sendLink(String link, String to) throws EmailException;
}
