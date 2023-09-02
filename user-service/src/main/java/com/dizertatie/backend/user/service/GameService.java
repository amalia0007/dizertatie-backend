package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.model.Game;
import com.dizertatie.backend.user.util.RequestUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "game-service")
public interface GameService {

    @GetMapping("/games")
    List<Game> getGames(@RequestHeader("Authorization") String token);

    default List<Game> getGames() {
        return getGames(RequestUtil.getCurrentHttpRequest().getHeader("Authorization"));
    }

    @GetMapping("/searchGameById")
    Game findGameById(@RequestParam("id") Long id, @RequestHeader("Authorization") String token);

    default Game findGameById(Long id) {
        return findGameById(id, RequestUtil.getCurrentHttpRequest().getHeader("Authorization"));
    }

    @PostMapping(value = "/updateGame", consumes = MediaType.APPLICATION_JSON_VALUE)
    Game save(@RequestBody Game game, @RequestHeader("Authorization") String token);

    default void save(Game game) {
        save(game, RequestUtil.getCurrentHttpRequest().getHeader("Authorization"));
    }

}
