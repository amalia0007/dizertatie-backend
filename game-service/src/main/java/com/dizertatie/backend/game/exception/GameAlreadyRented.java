package com.dizertatie.backend.game.exception;

public class GameAlreadyRented extends Exception {
    @Override
    public String getMessage() {
        return "You are currently renting this game";
    }
}
