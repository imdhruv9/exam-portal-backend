package com.testportal.online_test_portal.exception.custom;

public class InvalidCredentialException extends RuntimeException{
    public InvalidCredentialException(String message){
        super(message);
    }
}
