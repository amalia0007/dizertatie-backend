package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserServiceFallback implements UserService {

    @Override
    public Optional<User> findById(Long id, String token) {
        return Optional.empty();
    }
}
