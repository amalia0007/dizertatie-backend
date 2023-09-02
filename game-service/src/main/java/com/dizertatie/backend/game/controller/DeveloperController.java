package com.dizertatie.backend.game.controller;

import com.dizertatie.backend.game.model.Developer;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeveloperController {

    private final DeveloperService developerService;

    @Autowired
    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/addDevelopers")
    public List<Developer> addDeveloper(@RequestBody List<Developer> developerList) {
        return developerService.addCreators(developerList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/removeDeveloper/{id}")
    public void deleteDeveloper(@PathVariable(value = "id") Long id) {
        developerService.deleteDeveloperById(id);
    }

    @GetMapping("/findDeveloperByName")
    public ResponsePageList<Developer> findAuthorByNameOrLastName(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("query") String query
    ) {
        return developerService.findDeveloperByName(orderBy, direction, page, size, query);
    }

    @GetMapping("/checkIfDeveloperExists")
    public boolean checkIfDeveloperExist(String query) {
        return developerService.checkIfCreatorExists(query);
    }

    @GetMapping("/getDeveloperByName")
    public Developer getDeveloperByName(String query) {
        return developerService.getCreatorByName(query);
    }

    @GetMapping("/getDevelopersList")
    public List<Developer> getDevelopersList() {
        return developerService.getCreatorsList();
    }

}
