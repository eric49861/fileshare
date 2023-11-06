package com.eric.fileshare.exceptions.login;

public class EmailNotRegisterException extends LoginException{

    public EmailNotRegisterException(){}

    public EmailNotRegisterException(String message) {
        super(message);
    }
}
