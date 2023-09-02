package com.dizertatie.backend.user.controller;

import com.dizertatie.backend.user.exception.GameAlreadyRented;
import com.dizertatie.backend.user.exception.GameOutOfStock;
import com.dizertatie.backend.user.exception.UserHasPenaltiesException;
import com.dizertatie.backend.user.model.ChartObject;
import com.dizertatie.backend.user.model.StatusChart;
import com.dizertatie.backend.user.model.UsersRentals;
import com.dizertatie.backend.user.pojo.ResponsePageList;
import com.dizertatie.backend.user.service.EmailServiceImpl;
import com.dizertatie.backend.user.service.UserGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserGameController {

    @Autowired
    private UserGameService userGameService;

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @GetMapping("/userGames")
    @ResponseBody
    public List<UsersRentals> getUserGames() {
        return userGameService.getUserGames();
    }

    @PostMapping("/addUserGame")
    public void addUserGame(@RequestBody UsersRentals usersRentals) throws UserHasPenaltiesException, GameOutOfStock, GameAlreadyRented {
        userGameService.addUserGame(usersRentals);
        emailServiceImpl.sendRentalEmail(usersRentals);
    }

    @GetMapping("/reminder")
    public void reminder() {
        userGameService.sendReminder();
    }

    @DeleteMapping("/removeUserGame/{id}")
    public void removeUserGame(@PathVariable(value = "id") Long id) {
        userGameService.removeById(id);
    }

    @GetMapping("/getRentedGames")
    public ResponsePageList<UsersRentals> getRentedGames(
            @RequestParam("orderBy") String orderBy,
            @RequestParam("direction") String direction,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("id") String id
    ) {
        return userGameService.getRentedGames(orderBy, direction, page, size, id);
    }

    @PostMapping("/returnGame")
    public void returnGame(@RequestParam("id") Long userId) {
        userGameService.returnRentedGame(userId);
    }

    @GetMapping("/populateChart")
    public ResponseEntity<List<ChartObject>> populateChart() {
        return new ResponseEntity<>(userGameService.populateChart(), HttpStatus.OK);
    }

    @GetMapping("/populateStatus")
    public ResponseEntity<List<StatusChart>> populateStatus() {
        return new ResponseEntity<>(userGameService.populateStatusChart(), HttpStatus.OK);
    }

    @PostMapping("/sendAlmostReturnDateEmail")
    public void sendAlmostReturnEmail() {
        userGameService.sendReminder();

    }
}

