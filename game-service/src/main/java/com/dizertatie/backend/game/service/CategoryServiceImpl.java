package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.model.Category;
import com.dizertatie.backend.game.model.Direction;
import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.repository.CategoryRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> addCategory(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String query) {
        return categoryRepository.findAll().stream().filter(category -> category.getName().toLowerCase().contains(query.toLowerCase())).findFirst().orElse(null);
    }

    @Override
    public boolean checkIfCategoryExists(String query) {
        return categoryRepository.findAll().stream().anyMatch(category -> category.getName().toLowerCase().contains(query.toLowerCase()));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public ResponsePageList<Category> findCategoryByName(String orderBy, String direction, int page, int size, String query) {

        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        }

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode()) || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Predicate<Category> containName = genre -> genre.getName().equalsIgnoreCase(query);
        List<Category> authorList = categoryRepository.findAll(sort).stream().filter(containName).collect(Collectors.toList());

        PagedListHolder<Category> pagedListHolder = new PagedListHolder<>(authorList);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<Category> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());
        return response;

    }

    @Override
    @ExceptionHandler(PaginationSortingException.class)
    public ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex) {
        PagingSortingErrorResponse pagingSortingErrorResponse = new PagingSortingErrorResponse();
        pagingSortingErrorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        pagingSortingErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(pagingSortingErrorResponse, HttpStatus.OK);
    }
}
