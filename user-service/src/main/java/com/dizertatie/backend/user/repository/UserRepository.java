package com.dizertatie.backend.user.repository;

import com.dizertatie.backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("" +
            "select u " +
            "from User u " +
            "where u.company.company_id = :companyId " +
            "and " +
            "(" +
            "   :userName is null " +
            "   or lower(u.firstName) like concat('%',lower(:userName),'%') " +
            "   or lower(u.lastName) like concat('%',lower(:userName),'%') " +
            ")"
    )
    Page<User> findAllByCompanyAndUserName(Long companyId, String userName, Pageable pageable);

    @Query("" +
            "select u " +
            "from User u " +
            "where " +
            "(" +
            "   :userName is null " +
            "   or lower(u.firstName) like concat('%',lower(:userName),'%') " +
            "   or lower(u.lastName) like concat('%',lower(:userName),'%') " +
            ")"
    )
    Page<User> findAllByUserName(String userName, Pageable pageable);
}
