package ru.yandex.practicum.sleeptracker.model;

public enum SleepQuality {
    GOOD("Хорошее"),
    NORMAL("Нормальное"),
    BAD("Плохое");

    private final String description;

    SleepQuality(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
