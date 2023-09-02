package com.dizertatie.backend.user.controller;

import com.dizertatie.backend.user.model.Role;
import com.dizertatie.backend.user.model.User;
import com.dizertatie.backend.user.pojo.ResponsePageList;
import com.dizertatie.backend.user.repository.RoleRepository;
import com.dizertatie.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/allusers")
    List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    Optional<User> findOne(Long id) {
        return userService.findById(id);
    }

    @GetMapping("/findUserByEmail")
    ResponseEntity<User> findUserByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/changeRole/{id}")
    public void changeRole(@PathVariable(value = "id") Long userId) {
        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Role userRole = roleRepository.findByType("ROLE_ADMIN");
            user.setAdmin(true);
            user.setRoles(new HashSet<>(Collections.singleton(userRole)));
            userService.save(user);
        } else {
            throw new IllegalArgumentException("The user with this id: " + userId + " does not exist!");
        }
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/paginatedUsers")
    public ResponseEntity<ResponsePageList<User>> findPaginatedUsers(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("query") String query
    ) {
        return new ResponseEntity<>(userService.findPaginatedUsers(orderBy, direction, page, size, query), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/v2/paginatedUsers")
    public ResponsePageList<User> findPaginatedUsers(@RequestParam("userName") String userName, Pageable pageable) {
        return userService.findPaginatedUsers(userName, pageable);
    }

    @PutMapping("/updateUser")
    public void updateUser(@RequestBody User user) {
        userService.save(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/updateUserBanned")
    public void updateUserBanned(@RequestBody User user) {
        userService.updateUserBanned(user);
    }

    @Deprecated
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/makeAdmin")
    public void makeAdmin(@RequestBody User user) {
        userService.makeAdmin(user);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/v2/makeAdmin/{id}")
    public void makeAdminV2(@PathVariable("id") Long id) {
        userService.makeAdmin(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/updateUserBanUntil")
    public void updateUserBanUntil(@RequestBody User user) {
        userService.updateUserBanUntil(user);
    }

    @PutMapping("/updateUserImg")
    public void updateUserImg(@RequestBody User user) {
        userService.saveUserImg(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/addPenalty")
    public void addPenalty(@RequestBody User user) {
        userService.addOnePenalty(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/removePenalty")
    public void removePenalty(@RequestBody User user,
                              @RequestParam("penaltyId") String penaltyId) {
        userService.removeOnePenalty(user, penaltyId);
    }

    @PostMapping("/clearPenalties")
    public void clearPenalties(@RequestBody User user) {
        userService.clearPenalties(user);
    }

    @PostMapping("/checkForPenalties")
    public void checkForPenalties(@RequestBody User user) {
        userService.checkForPenalties(user);
    }


}
