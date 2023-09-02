package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.Developer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;

import java.util.List;

public interface DeveloperService {

    List<Developer> getCreatorsList();

    Developer getCreatorByName(String query);

    List<Developer> addCreators(List<Developer> developer);

    boolean checkIfCreatorExists(String query);

    void deleteDeveloperById(Long id);

    ResponsePageList<Developer> findDeveloperByName(String orderBy, String direction, int page, int size, String query);

    @ExceptionHandler(PaginationSortingException.class)
    ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex);
}
