package com.dizertatie.backend.game.controller;


import com.dizertatie.backend.game.model.GameRating;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PostMapping("/addRating")
    public void addRatings(@RequestBody GameRating gameRating,
                           @RequestParam("id") Long id) {
        ratingService.addRating(gameRating, id);
    }

    @GetMapping("/paginatedRatings")
    public ResponseEntity<ResponsePageList<GameRating>> findPaginatedComments(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("gameId") String gameId
    ) {
        return new ResponseEntity<>(ratingService.findPaginatedRatings(orderBy, direction, page, size, gameId), HttpStatus.OK);
    }
}
