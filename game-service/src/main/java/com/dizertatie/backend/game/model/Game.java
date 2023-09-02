package com.dizertatie.backend.game.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String idSerial;

    @NotBlank
    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Developer> developers;

    @NotBlank
    private String gameStudio;

    private Integer year;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> categories;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private GameCover img;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameRating> gameRatings;

    private Double averageStars;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int stock;

    private Long rentalCount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(Long rentalCount) {
        this.rentalCount = rentalCount;
    }

    public void increaseRentalCount() {
        this.rentalCount++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdSerial() {
        return idSerial;
    }

    public void setIdSerial(String isbn) {
        this.idSerial = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public String getGameStudio() {
        return gameStudio;
    }

    public void setGameStudio(String publishingHouse) {
        this.gameStudio = publishingHouse;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public GameCover getImg() {
        if (img == null) {
            return GameCover.defaultVal();
        }
        return img;
    }

    public void setImg(GameCover img) {
        this.img = img;
    }

    public List<GameRating> getGameRatings() {
        return gameRatings;
    }

    public void setGameRatings(List<GameRating> gameRatings) {
        this.gameRatings = gameRatings;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void addStock() {
        this.stock++;
    }

    public void decreseStock() {
        this.stock--;
    }

    public Double getAverageStars() {
        return averageStars;
    }

    public void setAverageStars() {
        double result = 0d;
        List<GameRating> gameRatings = getGameRatings();
        if (gameRatings.isEmpty()) {
            this.averageStars = 0d;
            return;
        } else {
            int number = gameRatings.size();
            for (GameRating i : gameRatings) {
                result = i.getValue() + result;
            }
            this.averageStars = (result / number);
        }
    }

    public Game() {
    }

    public Game(@NotBlank String idSerial, @NotBlank String title, List<Developer> developers, @NotBlank String gameStudio, Integer year, List<Category> categories, GameCover img, List<GameRating> gameRatings, String description, int stock) {
        this.idSerial = idSerial;
        this.title = title;
        this.developers = developers;
        this.gameStudio = gameStudio;
        this.year = year;
        this.categories = categories;
        this.img = img;
        this.gameRatings = gameRatings;
        this.description = description;
        this.stock = stock;
    }
}
