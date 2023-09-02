package com.dizertatie.backend.user.repository;


import com.dizertatie.backend.user.model.Game;
import com.dizertatie.backend.user.model.User;
import com.dizertatie.backend.user.model.UsersRentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameRepository extends JpaRepository<UsersRentals, Long> {
    UsersRentals findByGameAndUser(Game game, User user);
}
