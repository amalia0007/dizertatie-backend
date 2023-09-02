package com.dizertatie.backend.user.repository;


import com.dizertatie.backend.user.model.GameCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUploadRepository extends JpaRepository<GameCover, Long> {
}
