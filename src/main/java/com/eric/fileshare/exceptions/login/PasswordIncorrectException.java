package com.eric.fileshare.exceptions.login;

public class PasswordIncorrectException extends LoginException{

    public PasswordIncorrectException(){}

    public PasswordIncorrectException(String message) {
        super(message);
    }
}
