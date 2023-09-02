package com.dizertatie.backend.user.model;

public enum OrderBy {
    ID("id"), TITLE("title"), VALUE("averageStars"), RENTAL_COUNT("rentalCount");
    private String OrderByCode;

    OrderBy(String orderBy) {
        this.OrderByCode = orderBy;
    }

    public String getOrderByCode() {
        return this.OrderByCode;
    }
}
