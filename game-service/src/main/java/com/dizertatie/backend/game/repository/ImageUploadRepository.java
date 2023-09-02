package com.dizertatie.backend.game.repository;


import com.dizertatie.backend.game.model.GameCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUploadRepository extends JpaRepository<GameCover, Long> {
}
