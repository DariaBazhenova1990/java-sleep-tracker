package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageSessionDurationCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Средняя продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, 0.0);
        }

        double avgDuration = sessions.stream()
                .filter(s -> s.getStartDateTime() != null && s.getEndDateTime() != null)
                .mapToDouble(s -> Duration.between(s.getStartDateTime(), s.getEndDateTime()).toMinutes())
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult(DESCRIPTION, avgDuration);
    }
}
