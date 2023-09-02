package com.dizertatie.backend.user.exception;

@SuppressWarnings("serial")
public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message) {
        super(message);
    }

}
