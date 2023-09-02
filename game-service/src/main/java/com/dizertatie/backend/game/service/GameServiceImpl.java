package com.dizertatie.backend.game.service;

import com.dizertatie.backend.game.exception.PaginationSortingException;
import com.dizertatie.backend.game.exception.PagingSortingErrorResponse;
import com.dizertatie.backend.game.model.*;
import com.dizertatie.backend.game.pojo.ResponsePageList;
import com.dizertatie.backend.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public void save(Game game) {
        if (game.getAverageStars() == null) {
            game.setAverageStars();
        }
        gameRepository.saveAndFlush(game);
    }

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public void remove(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    public Sort sorting(String direction, String orderBy) {

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
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()) || orderBy.equals(OrderBy.VALUE.getOrderByCode()) || orderBy.equals(OrderBy.RENTAL_COUNT.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        return sort;
    }

    @Override
    public ResponsePageList<Game> findPaginatedGames(String orderBy, String direction, int page, int size, String query) {

        Sort sort = sorting(direction, orderBy);

        Predicate<Game> titleExist = game -> game.getTitle().toLowerCase().contains(query.toLowerCase());
        Predicate<Category> foundInCategories = category -> category.getName().toLowerCase().contains(query.toLowerCase());
        Predicate<Game> categoryExists = game -> game.getCategories().stream().anyMatch(foundInCategories);
        Predicate<Developer> foundInCreatorName = author -> author.getName().toLowerCase().contains(query.toLowerCase());
        Predicate<Game> authorExist = game -> game.getDevelopers().stream().anyMatch(foundInCreatorName);
        List<Game> list = gameRepository.findAll(sort).stream().filter(titleExist.or(categoryExists).or(authorExist)).collect(Collectors.toList());

        PagedListHolder<Game> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);

        ResponsePageList<Game> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());

        return response;

    }

    @Override
    public ResponsePageList<Game> findPreferredGames(String orderBy, String direction, int page, int size, String id) {

        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()) || orderBy.equals(OrderBy.VALUE.getOrderByCode()) || orderBy.equals(OrderBy.RENTAL_COUNT.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()) || orderBy.equals(OrderBy.VALUE.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        User user = userService.findById(Long.parseLong(id)).orElse(null);
        assert user != null;
        List<Category> categoryList = user.getCategories();
        List<Game> uniqueList = new ArrayList<>();
        for (Category currentCategory : categoryList) {
            Predicate<Category> categoryCondition = category -> category.getName().toLowerCase().contains(currentCategory.getName().toLowerCase());
            Predicate<Game> categoryMatch = game -> game.getCategories().stream().anyMatch(categoryCondition);
            List<Game> list = gameRepository.findAll().stream().filter(categoryMatch).collect(Collectors.toList());
            List<Game> updated = list.stream().filter(game -> !uniqueList.contains(game)).collect(Collectors.toList());

            uniqueList.addAll(updated);

            if (orderBy.equals(OrderBy.TITLE.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getTitle));
            } else if (orderBy.equals(OrderBy.TITLE.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getTitle).reversed());
            } else if (orderBy.equals(OrderBy.ID.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getId));
            } else if (orderBy.equals(OrderBy.ID.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getId).reversed());
            } else if (orderBy.equals(OrderBy.VALUE.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getAverageStars));
            } else if (orderBy.equals(OrderBy.VALUE.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getAverageStars).reversed());
            }


        }

        PagedListHolder<Game> pagedListHolder = new PagedListHolder<>(new ArrayList<>(uniqueList));
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<Game> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());
        return response;

    }

    @Override
    public ResponsePageList<Game> findSameCategoryGames(String orderBy, String direction, int page, int size, String id) {


        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()) || orderBy.equals(OrderBy.VALUE.getOrderByCode()) || orderBy.equals(OrderBy.RENTAL_COUNT.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.ID.getOrderByCode()) || orderBy.equals(OrderBy.TITLE.getOrderByCode()) || orderBy.equals(OrderBy.VALUE.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Game gameById = gameRepository.findGameById(Long.parseLong(id));
        List<Category> categoryList = gameById.getCategories();
        List<Game> uniqueList = new ArrayList<>();

        for (Category currentCategory : categoryList) {
            Predicate<Category> foundInCategory = category -> category.getName().toLowerCase().contains(currentCategory.getName().toLowerCase());
            Predicate<Game> categoryExists = b -> b.getCategories().stream().anyMatch(foundInCategory);
            List<Game> list = gameRepository.findAll().stream().filter(categoryExists.and(game -> game.getId() != Long.parseLong(id))).collect(Collectors.toList());
            List<Game> updated = list.stream().filter(game -> !uniqueList.contains(game)).collect(Collectors.toList());

            uniqueList.addAll(updated);

            if (orderBy.equals(OrderBy.TITLE.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getTitle));
            } else if (orderBy.equals(OrderBy.TITLE.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getTitle).reversed());
            } else if (orderBy.equals(OrderBy.ID.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getId));
            } else if (orderBy.equals(OrderBy.ID.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getId).reversed());
            } else if (orderBy.equals(OrderBy.VALUE.getOrderByCode()) & direction.equals(Direction.ASCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getAverageStars));
            } else if (orderBy.equals(OrderBy.VALUE.getOrderByCode()) & direction.equals(Direction.DESCENDING.getDirectionCode())) {
                uniqueList.sort(Comparator.comparing(Game::getAverageStars).reversed());
            }

        }

        PagedListHolder<Game> pagedListHolder = new PagedListHolder<>(new ArrayList<>(uniqueList));
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<Game> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());

        return response;

    }


    @ExceptionHandler(PaginationSortingException.class)
    public ResponseEntity<PagingSortingErrorResponse> exceptionHandler(Exception ex) {
        PagingSortingErrorResponse pagingSortingErrorResponse = new PagingSortingErrorResponse();
        pagingSortingErrorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        pagingSortingErrorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(pagingSortingErrorResponse, HttpStatus.OK);
    }

    @Override
    public Game findGameById(Long id) {
        System.out.println("Searching game " + id);
        return gameRepository.findGameById(id);
    }

}
