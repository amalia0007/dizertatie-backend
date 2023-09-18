package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.model.Game;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "game-service", fallback = GameServiceFallback.class)
public interface GameService {

    @GetMapping("/games")
    List<Game> getGames(@RequestHeader("Authorization") String token);

    @GetMapping("/searchGameById")
    Game findGameById(@RequestParam("id") Long id, @RequestHeader("Authorization") String token);

    @PostMapping(value = "/updateGame", consumes = MediaType.APPLICATION_JSON_VALUE)
    Game save(@RequestBody Game game, @RequestHeader("Authorization") String token);

}
