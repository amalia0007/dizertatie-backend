package com.dizertatie.backend.game.repository;

import com.dizertatie.backend.game.model.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRatingRepository extends JpaRepository<GameRating, Long> {
}
