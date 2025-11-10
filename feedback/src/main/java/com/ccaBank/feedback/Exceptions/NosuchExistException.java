package com.ccaBank.feedback.Exceptions;

public class NosuchExistException extends RuntimeException {

    private String message;

    public NosuchExistException(){}

    public NosuchExistException(String msg) {
        super(msg);
        this.message=msg;
    }
}
