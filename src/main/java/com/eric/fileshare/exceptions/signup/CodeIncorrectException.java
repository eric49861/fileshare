package com.eric.fileshare.exceptions.signup;

public class CodeIncorrectException extends SignupException{
    private String message;

    public CodeIncorrectException(){}

    public CodeIncorrectException(String message){
        super(message);
        this.message = message;
    }
}
