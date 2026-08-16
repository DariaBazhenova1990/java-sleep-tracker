package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeIdentifier implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Классификация пользователя по хронотипу";
    private static final LocalTime LARK_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_END = LocalTime.of(7, 0);
    private static final LocalTime OWL_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_END = LocalTime.of(9, 0);
    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private static String getSessionChronotype(SleepingSession s) {
        LocalTime start = s.getStartDateTime().toLocalTime();
        LocalTime end = s.getEndDateTime().toLocalTime();

        boolean isLarkStart = start.isBefore(LARK_START);
        boolean isLarkEnd = end.isBefore(LARK_END);
        if (isLarkStart && isLarkEnd) {
            return Chronotype.LARK.getDescription();
        }

        boolean isOwlStart = start.isAfter(OWL_START) || start.isBefore(NIGHT_END);
        boolean isOwlEnd = end.isAfter(OWL_END);
        if (isOwlStart && isOwlEnd) {
            return Chronotype.OWL.getDescription();
        }

        return Chronotype.HUMMINGBIRD.getDescription();
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        String chronotype = "хронотип не возможно определить";
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(DESCRIPTION, chronotype);
        }
        chronotype = defineUserChronotype(sessions);
        return new SleepAnalysisResult(DESCRIPTION, chronotype);
    }

    private String defineUserChronotype(List<SleepingSession> sessions) {
        List<String> chronotypeByNights = sessions.stream()
                .filter(s -> s.getStartDateTime() != null && s.getEndDateTime() != null)
                .map(ChronotypeIdentifier::getSessionChronotype)
                .toList();

        Map<String, Long> countChronotypes = chronotypeByNights.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        long maxCount = countChronotypes.values().stream()
                .max(Long::compare)
                .orElse(0L);

        List<String> maxCountChronotypes = countChronotypes.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .toList();

        if (maxCountChronotypes.size() > 1) {
            return Chronotype.HUMMINGBIRD.getDescription();
        } else {
            return maxCountChronotypes.getFirst();
        }
    }

}
