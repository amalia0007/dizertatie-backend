package com.dizertatie.backend.game.repository;

import com.dizertatie.backend.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, PagingAndSortingRepository<Game, Long> {

    Game findGameById(Long id);

}
