package com.dizertatie.backend.user.exception;

public class GameAlreadyRented extends Exception {
    @Override
    public String getMessage() {
        return "You are currently renting this game";
    }

}
