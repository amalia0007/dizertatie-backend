package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.Category;
import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;

import java.util.List;

public interface CategoryService {

    List<Category> addCategory(List<Category> categories);

    List<Category> getCategoryList();

    Category getCategoryByName(String query);

    boolean checkIfCategoryExists(String query);

    void deleteCategoryById(Long id);

    ResponsePageList<Category> findCategoryByName(String orderBy, String direction, int page, int size, String query);

    @ExceptionHandler(PaginationSortingException.class)
    ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex);
}
