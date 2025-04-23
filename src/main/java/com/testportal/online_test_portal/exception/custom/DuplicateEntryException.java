package com.testportal.online_test_portal.exception.custom;

public class DuplicateEntryException extends RuntimeException{
    public DuplicateEntryException(String message){
        super(message);
    }
}
