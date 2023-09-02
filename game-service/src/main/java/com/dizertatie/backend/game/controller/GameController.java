package com.dizertatie.backend.game.controller;


import com.dizertatie.backend.game.service.GameService;
import com.dizertatie.backend.game.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dizertatie.backend.game.pojo.ResponsePageList;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public List<Game> get() {
        return gameService.getGames();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/addGame")
    public void addGame(@RequestBody Game game) {
        gameService.save(game);
    }

    @PostMapping("/updateGame")
    public void updateGame(@RequestBody Game game) {
        gameService.save(game);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/remove/{id}")
    public void removeGame(@PathVariable(value = "id") Long id) {
        gameService.remove(id);
    }

    @GetMapping("/paginatedGames")
    public ResponseEntity<ResponsePageList<Game>> findPaginatedGames(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("query") String query
    ) {
        return new ResponseEntity<>(gameService.findPaginatedGames(orderBy, direction, page, size, query), HttpStatus.OK);
    }

    @GetMapping("/preferredGames")
    public ResponseEntity<ResponsePageList<Game>> findPreferredGames(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("id") String id
    ) {
        return new ResponseEntity<>(gameService.findPreferredGames(orderBy, direction, page, size, id), HttpStatus.OK);
    }

    @GetMapping("/sameCategoryGames")
    public ResponseEntity<ResponsePageList<Game>> findSameCategoryGames(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("id") String id
    ) {
        return new ResponseEntity<>(gameService.findSameCategoryGames(orderBy, direction, page, size, id), HttpStatus.OK);
    }

    @GetMapping("/searchGameById")
    public Game searchGameById(@RequestParam("id") Long id) {
        return gameService.findGameById(id);
    }
}
