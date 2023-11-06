package com.eric.fileshare.exceptions.signup;

public class EmailAlreadyInUseException extends SignupException{

    private String message;

    public EmailAlreadyInUseException(){}

    public EmailAlreadyInUseException(String message){
        super(message);
        this.message = message;
    }
}
