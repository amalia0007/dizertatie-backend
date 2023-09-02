package com.dizertatie.backend.user.exception;

public class GameOutOfStock extends Exception {

    @Override
    public String getMessage() {
        return "The game is currently out of stock.";
    }
}
