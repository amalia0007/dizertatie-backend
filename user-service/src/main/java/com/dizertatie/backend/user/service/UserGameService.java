package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.exception.GameAlreadyRented;
import com.dizertatie.backend.user.exception.GameOutOfStock;
import com.dizertatie.backend.user.exception.UserHasPenaltiesException;
import com.dizertatie.backend.user.model.UsersRentals;
import com.dizertatie.backend.user.model.ChartObject;
import com.dizertatie.backend.user.model.StatusChart;
import com.dizertatie.backend.user.pojo.ResponsePageList;

import java.util.List;

public interface UserGameService {

    void addUserGame(UsersRentals usersRentals) throws UserHasPenaltiesException, GameOutOfStock, GameAlreadyRented;

    List<UsersRentals> getUserGames();

    void sendReminder();

    ResponsePageList<UsersRentals> getRentedGames(String orderBy, String direction, int page, int size, String id);

    void returnRentedGame(Long userGameId);

    void changeUserGamePenalty(Long userGameId);

    void removeById(Long userGameId);

    List<ChartObject> populateChart();

    List<StatusChart> populateStatusChart();
}
