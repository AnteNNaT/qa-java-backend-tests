package ru.geekbrains.enums;

public enum Category {
    FOOD(1, "Food"),
    ELECTRONIC(2,"Electronic"),
    NULL_CATEGORY(1000, "Non-existent");

    public final int id;
    public final String title;

    Category(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
