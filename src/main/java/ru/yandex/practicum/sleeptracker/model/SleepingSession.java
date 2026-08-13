package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SleepingSession {
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime startDateTime, LocalDateTime endDateTime, SleepQuality quality) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.quality = quality;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        SleepingSession that = (SleepingSession) object;
        return Objects.equals(startDateTime, that.startDateTime)
                && Objects.equals(endDateTime, that.endDateTime)
                && Objects.equals(quality, that.quality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime, quality);
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "startTime=" + startDateTime +
                ", endTime=" + endDateTime +
                ", quality='" + quality + '\'' +
                '}';
    }
}
