package com.dizertatie.backend.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate penaltyAddedDate;

    public static int maxNumberOfPenalties = 2;

    public static int numberOfMonthsPenaltyExist = 6;

    public static int oneMonth = 1;

    public Penalty() {
    }

    public Penalty(LocalDate penaltyAddedDate) {
        this.penaltyAddedDate = penaltyAddedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getPenaltyAddedDate() {
        return penaltyAddedDate;
    }

    public void setPenaltyAddedDate(LocalDate penaltyAddedDate) {
        this.penaltyAddedDate = penaltyAddedDate;
    }
}
