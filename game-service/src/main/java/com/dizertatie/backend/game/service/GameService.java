package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.Game;
import com.dizertatie.backend.game.pojo.ResponsePageList;

import java.util.List;

public interface GameService {

    void save(Game b);

    List<Game> getGames();

    void remove(Long gameId);

    ResponsePageList<Game> findPaginatedGames(String orderBy, String direction, int page, int size, String query);

    ResponsePageList<Game> findPreferredGames(String orderBy, String direction, int page, int size, String id);

    ResponsePageList<Game> findSameCategoryGames(String orderBy, String direction, int page, int size, String id);

    Game findGameById(Long id);

}
