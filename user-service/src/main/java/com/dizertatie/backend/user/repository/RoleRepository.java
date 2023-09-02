package com.dizertatie.backend.user.repository;


import com.dizertatie.backend.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByType(String type);
    Optional<Role> findById(Long id);
}
