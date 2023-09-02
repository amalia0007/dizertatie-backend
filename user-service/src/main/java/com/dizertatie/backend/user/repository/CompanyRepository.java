package com.dizertatie.backend.user.repository;

import com.dizertatie.backend.user.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByName(String name);
    @Query("select c from Company c")
    List<Company> findAll();
}
