package com.dizertatie.backend.game.service;


import com.dizertatie.backend.game.model.Direction;
import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.Developer;
import com.dizertatie.backend.game.model.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.repository.DeveloperRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Override
    public List<Developer> getCreatorsList() {
        return developerRepository.findAll();
    }

    @Override
    public Developer getCreatorByName(String query) {
        return developerRepository.findAll().stream().filter(author -> author.getName().toLowerCase().contains(query.toLowerCase())).findFirst().orElse(null);
    }

    @Override
    public List<Developer> addCreators(List<Developer> developers) {
        return developerRepository.saveAll(developers);
    }

    @Override
    public boolean checkIfCreatorExists(String query) {
        return developerRepository.findAll().stream().anyMatch(dev -> dev.getName().toLowerCase().contains(query.toLowerCase()));
    }

    @Override
    public void deleteDeveloperById(Long id) {
        developerRepository.deleteById(id);
    }

    @Override
    public ResponsePageList<Developer> findDeveloperByName(String orderBy, String direction, int page, int size, String query) {

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

        Predicate<Developer> containName = dev -> dev.getName().equalsIgnoreCase(query);
        List<Developer> developerList = developerRepository.findAll(sort).stream().filter(containName).collect(Collectors.toList());

        PagedListHolder<Developer> pagedListHolder = new PagedListHolder<>(developerList);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<Developer> response = new ResponsePageList<>();
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
