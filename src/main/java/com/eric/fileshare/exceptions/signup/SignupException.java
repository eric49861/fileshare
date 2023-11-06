package com.eric.fileshare.exceptions.signup;

public class SignupException extends Exception{

    private String message;

    public SignupException(){}

    public SignupException(String message) {
        super(message);
        this.message = message;
    }
}
