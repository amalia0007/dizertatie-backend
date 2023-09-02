package com.dizertatie.backend.user.exception;

public class UserHasPenaltiesException extends Exception {
    @Override
    public String getMessage() {

        return "User has reached the number of maximum penalties and cannot rent more games!";
    }
}
