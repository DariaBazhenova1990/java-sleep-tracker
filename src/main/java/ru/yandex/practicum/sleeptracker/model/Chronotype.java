package ru.yandex.practicum.sleeptracker.model;

public enum Chronotype {
    LARK("Жаворонок"),
    OWL("Сова"),
    PEGEON("Голубь");

    private final String description;

    Chronotype(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
