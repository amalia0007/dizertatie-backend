package com.dizertatie.backend.user.repository;

import com.dizertatie.backend.user.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    @Query("select u from VerificationToken t join t.user u where u.email = :email and t.expirationDate >= CURRENT_TIMESTAMP")
    VerificationToken findActiveByUserEmail(@Param("email") String email);
}
