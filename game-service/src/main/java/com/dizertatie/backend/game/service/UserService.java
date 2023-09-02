package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.User;
import com.dizertatie.backend.game.util.RequestUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserService {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Optional<User> findById(@RequestParam("id") Long id, @RequestHeader("Authorization") String token);

    default Optional<User> findById(Long id) {
        String token = RequestUtil.getCurrentHttpRequest().getHeader("Authorization");
        return findById(id, token);
    }

}
