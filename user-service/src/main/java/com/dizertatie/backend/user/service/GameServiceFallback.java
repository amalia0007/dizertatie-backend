package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class GameServiceFallback implements GameService {

    @Override
    public List<Game> getGames(String token) {
        return Collections.emptyList();
    }

    @Override
    public Game findGameById(Long id, String token) {
        throw new IllegalStateException("Game service is down!");
    }

    @Override
    public Game save(Game game, String token) {
        throw new IllegalStateException("Game service is down!");
    }

}
