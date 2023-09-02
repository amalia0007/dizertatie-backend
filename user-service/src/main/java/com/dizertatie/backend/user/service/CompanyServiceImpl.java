package com.dizertatie.backend.user.service;


import com.dizertatie.backend.user.repository.CompanyRepository;
import com.dizertatie.backend.user.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }
}
