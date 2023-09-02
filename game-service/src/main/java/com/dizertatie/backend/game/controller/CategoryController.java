package com.dizertatie.backend.game.controller;

import com.dizertatie.backend.game.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.service.CategoryService;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCategory")
    public List<Category> addCategories(@RequestBody List<Category> categoryList) {
        return categoryService.addCategory(categoryList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/removeCategory/{id}")
    public void deleteCategory(@PathVariable(value = "id") Long id) {
        categoryService.deleteCategoryById(id);
    }

    @GetMapping("/findCategoryByName")
    public ResponsePageList<Category> findCategoryByName(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("query") String query
    ) {
        return categoryService.findCategoryByName(orderBy, direction, page, size, query);
    }

    @GetMapping("/checkIfCategoryExist")
    public boolean checkIfCategoryExist(String query) {
        return categoryService.checkIfCategoryExists(query);
    }

    @GetMapping("/getCategoryByName")
    public Category getCategoryByName(String query) {
        return categoryService.getCategoryByName(query);
    }

    @GetMapping("/getCategoryList")
    public List<Category> getCategoriesList() {
        return categoryService.getCategoryList();
    }

}
