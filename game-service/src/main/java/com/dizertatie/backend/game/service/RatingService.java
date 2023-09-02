package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.GameRating;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;

public interface RatingService {
    void addRating(GameRating gameRating, Long id);

    @ExceptionHandler(PaginationSortingException.class)
    ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex);

    ResponsePageList<GameRating> findPaginatedRatings(String orderBy, String direction, int page, int size, String gameId);
}
