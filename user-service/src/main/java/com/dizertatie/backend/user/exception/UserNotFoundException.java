package com.dizertatie.backend.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found!");
    }
}
