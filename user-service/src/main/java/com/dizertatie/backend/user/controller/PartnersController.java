package com.dizertatie.backend.user.controller;

import com.dizertatie.backend.user.model.Company;
import com.dizertatie.backend.user.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company")
public class PartnersController {
    @Autowired
    CompanyService companyService;

    @GetMapping
    public List<Company> getCompaniesList() {
        return companyService.getCompanies();
    }

}
