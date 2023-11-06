package com.eric.fileshare.exceptions.login;


public class EmailNotFoundException extends LoginException {

    private String message;

    public EmailNotFoundException(){}

    public EmailNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
