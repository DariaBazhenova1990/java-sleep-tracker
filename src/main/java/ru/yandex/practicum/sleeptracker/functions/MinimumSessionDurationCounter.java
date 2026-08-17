package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinimumSessionDurationCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Минимальная продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, 0L);
        }

        long minDuration = sessions.stream()
                .filter(s -> s.getStartDateTime() != null && s.getEndDateTime() != null)
                .mapToLong(s -> Duration.between(s.getStartDateTime(), s.getEndDateTime()).toMinutes())
                .min()
                .orElse(0L);

        return new SleepAnalysisResult(DESCRIPTION, minDuration);
    }
}
