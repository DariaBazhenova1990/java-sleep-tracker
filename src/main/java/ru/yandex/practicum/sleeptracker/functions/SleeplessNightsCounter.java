package ru.yandex.practicum.sleeptracker.functions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;

public class SleeplessNightsCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Количество бессонных ночей";
    private static final int MIDDAY = 12;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, 0);
        }

        long totalNights = countTotalNights(sessions);
        long sleepNights = countSleepNight(sessions);

        return new SleepAnalysisResult(DESCRIPTION, totalNights - sleepNights);
    }

    long countTotalNights(List<SleepingSession> sessions) {
        LocalDateTime firstSessionStart = sessions.stream()
                .map(SleepingSession::getStartDateTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime lastSessionEnd = sessions.stream()
                .map(SleepingSession::getEndDateTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDate startDate = firstSessionStart.toLocalDate();
        LocalDate endDate = lastSessionEnd.toLocalDate();
        long nights = ChronoUnit.DAYS.between(startDate, endDate);

        if (firstSessionStart.getHour() >= MIDDAY) {
            return nights;
        } else {
            return nights + 1;
        }

    }

    private long countSleepNight(List<SleepingSession> sessions) {
        return sessions.stream()
                .filter(s -> {
                            LocalDate nightDate = s.getEndDateTime().toLocalDate();
                            LocalDateTime nightStart = nightDate.atStartOfDay();
                            LocalDateTime nightEnd = nightStart.plusHours(6);
                            return s.getEndDateTime().isAfter(nightStart) && s.getStartDateTime().isBefore(nightEnd);
                        }
                )
                .count();
    }

}
