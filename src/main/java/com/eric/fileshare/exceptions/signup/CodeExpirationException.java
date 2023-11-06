package com.eric.fileshare.exceptions.signup;

public class CodeExpirationException extends SignupException{
    private String message;

    public CodeExpirationException(){}

    public CodeExpirationException(String message){
        super(message);
        this.message = message;
    }
}
