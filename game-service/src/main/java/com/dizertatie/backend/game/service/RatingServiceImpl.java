package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.Direction;
import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.Game;
import com.dizertatie.backend.game.model.GameRating;
import com.dizertatie.backend.game.model.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.repository.GameRepository;
import com.dizertatie.backend.game.repository.RatingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingService;

    @Autowired
    GameRepository gameRepository;

    @Override
    public void addRating(GameRating gameRating, Long id) {
        Game game = gameRepository.findGameById(id);
        game.getGameRatings().add(gameRating);
        game.setAverageStars();
        gameRepository.saveAndFlush(game);
    }

    @Override
    public ResponsePageList<GameRating> findPaginatedRatings(String orderBy, String direction, int page, int size, String gameId) {
        if (direction.equals("ASC")) {
        }
        if (direction.equals("DESC")) {
        }

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode()) || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Game game = gameRepository.findById(Long.parseLong(gameId)).orElse(null);
        assert game != null;
        List<GameRating> gameRatingList = new ArrayList<>(game.getGameRatings());

        PagedListHolder<GameRating> pagedListHolder = new PagedListHolder<>(gameRatingList);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<GameRating> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());
        return response;

    }

    @Override
    @ExceptionHandler(PaginationSortingException.class)
    public ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex) {
        PagingSortingErrorResponse pagingSortingErrorResponse = new PagingSortingErrorResponse();
        pagingSortingErrorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        pagingSortingErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(pagingSortingErrorResponse, HttpStatus.OK);
    }

}
