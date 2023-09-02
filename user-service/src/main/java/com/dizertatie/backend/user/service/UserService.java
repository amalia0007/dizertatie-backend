package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.exception.EmailExistsException;
import com.dizertatie.backend.user.model.RegistrationDTO;
import com.dizertatie.backend.user.model.User;
import com.dizertatie.backend.user.pojo.ResponsePageList;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> findAll();

    void save(User user);

    Optional<User> findById(Long userId);

    User registerNewUserAccount(RegistrationDTO userDto) throws EmailExistsException;

    void delete(User user);

    ResponsePageList<User> findPaginatedUsers(String orderBy, String direction, int page, int size, String query);

    User findUserByEmail(String email);

    void clearPenalties(User user);

    void removeOnePenalty(User u, String penaltyId);

    void addOnePenalty(User u);

    void checkForPenalties(User user);

    void saveUserImg(User user);

    void updateUserBanned(User user);

    void updateUserBanUntil(User user);

    void makeAdmin(User user);

    void saveNewPassword(String email, String password);

    void makeAdmin(long userId);

    ResponsePageList<User> findPaginatedUsers(String userName, Pageable pageable);
}
