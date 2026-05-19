package com.soufiane.Library.exception;

public class DuplicateResourceException extends  RuntimeException{
    public DuplicateResourceException(String message){
        super(message);
    }
}
