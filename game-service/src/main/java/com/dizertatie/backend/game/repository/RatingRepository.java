package com.dizertatie.backend.game.repository;


import com.dizertatie.backend.game.model.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<GameRating, Long> {
}
