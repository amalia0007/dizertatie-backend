package com.dizertatie.backend.user.service;


import com.dizertatie.backend.user.exception.GameAlreadyRented;
import com.dizertatie.backend.user.exception.GameOutOfStock;
import com.dizertatie.backend.user.exception.PaginationSortingException;
import com.dizertatie.backend.user.exception.UserHasPenaltiesException;
import com.dizertatie.backend.user.model.*;
import com.dizertatie.backend.user.pojo.ResponsePageList;
import com.dizertatie.backend.user.repository.UserGameRepository;
import com.dizertatie.backend.user.util.RequestUtil;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserGameServiceImpl implements UserGameService {

    private final UserGameRepository userGameRepository;

    private final GameService gameService;

    private final EmailServiceImpl emailServiceImpl;

    public UserGameServiceImpl(UserGameRepository userGameRepository, GameService gameService, EmailServiceImpl emailServiceImpl) {
        this.userGameRepository = userGameRepository;
        this.gameService = gameService;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    @Transactional
    public void addUserGame(UsersRentals usersRentals) throws UserHasPenaltiesException, GameOutOfStock, GameAlreadyRented {

        //check if we dont have any more games of this type
        //we use gameService because we want to check the stack value from database, not from the request we sended
        Game game = gameService.findGameById(usersRentals.getGame().getId(), authToken());

        if (game.getStock() <= 0)
            throw new GameOutOfStock();

        if (userGameRepository.findByGameAndUser(usersRentals.getGame(), usersRentals.getUser()) != null)
            throw new GameAlreadyRented();


        if (usersRentals.getUser().getPenalties().size() < Penalty.maxNumberOfPenalties) {

            //when we rent a game the stock should decrease with one unit
            //we use gameService so the value will change in database
            game.decreseStock();
            game.increaseRentalCount();
            usersRentals.setReturn_date(LocalDate.now().plusDays(10));

            gameService.save(game, authToken());
            userGameRepository.save(usersRentals);
        } else
            //throws com.dizertatie.backend.game.exception and doesn`t save userGame instance if user has 2 penalties
            throw new UserHasPenaltiesException();
    }

    @Override
    public List<UsersRentals> getUserGames() {
        return userGameRepository.findAll();
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendReminder() {
        List<UsersRentals> list = userGameRepository.findAll();
        list.forEach(t -> {
            if (t.getReturn_date().plusDays(1L).equals(LocalDate.now()))
                emailServiceImpl.sendAlmostReturnDateEmail(t);
        });
    }

    @Override
    public ResponsePageList<UsersRentals> getRentedGames(String orderBy, String direction, int page, int size, String id) {
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

        Predicate<UsersRentals> byId = userGame -> userGame.getUser().getId().toString().equals(id);
        List<UsersRentals> list = userGameRepository.findAll(sort).stream().filter(byId).collect(Collectors.toList());

        PagedListHolder<UsersRentals> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        ResponsePageList<UsersRentals> response = new ResponsePageList<>();
        response.setNrOfElements(pagedListHolder.getNrOfElements());
        response.setPageList(pagedListHolder.getPageList());
        return response;

    }


    @Override
    public void returnRentedGame(Long userGameId) {

        //when the game is returned, the stock should increase with one unit
        userGameRepository.findById(userGameId).ifPresent(t -> t.getGame().setStock(t.getGame().getStock() + 1));
        userGameRepository.deleteById(userGameId);
    }

    public void changeUserGamePenalty(Long userGameId) {
        userGameRepository.findById(userGameId).ifPresent(t -> {
            t.setGeneratedPenalty(true);
            userGameRepository.saveAndFlush(t);
        });
    }

    @Override
    public void removeById(Long userGameId) {
        userGameRepository.deleteById(userGameId);
    }

    @Override
    public List<ChartObject> populateChart() {

        List<ChartObject> chartObjectList = new ArrayList<>();
        Set<String> gameSet = new HashSet<>();
        List<UsersRentals> usersRentalsList = userGameRepository.findAll();
        usersRentalsList.forEach(userGame -> gameSet.add(userGame.getGame().getTitle()));

        for (String title : gameSet) {
            long count = 0L;
            List<Game> gameList = usersRentalsList.stream().map(UsersRentals::getGame).collect(Collectors.toList());
//            List<Category> categoryList = new ArrayList<>();
//            gameList.stream().map(Game::get).forEach(categoryList::addAll);
            for (Game game : gameList) {
                if (title.equals(game.getTitle())) {
                    count++;
                }
            }
            chartObjectList.add(new ChartObject(count, title));
        }
        return chartObjectList;
    }

    @Override
    public List<StatusChart> populateStatusChart() {

        List<StatusChart> statusCharts = new ArrayList<>();
        long countAvailable = gameService.getGames(authToken()).stream().mapToLong(Game::getStock).sum();
        long countBlocked = userGameRepository.findAll().stream().filter(UsersRentals::isGeneratedPenalty).count();
        long countRented = userGameRepository.findAll().stream().filter(userGame -> !userGame.isGeneratedPenalty()).count();
        statusCharts.add(new StatusChart(countAvailable, "Available"));
//        statusCharts.add(new StatusChart(countBlocked, "Blocked"));
        statusCharts.add(new StatusChart(countRented, "Rented"));
        return statusCharts;
    }

    private String authToken() {
        return RequestUtil.getCurrentHttpRequest().getHeader("Authorization");
    }
}

