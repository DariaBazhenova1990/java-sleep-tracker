package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeIdentifier implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Классификация пользователя по хронотипу";
    private static final int LARK_START = 22;
    private static final int LARK_END = 7;
    private static final int OWL_START = 23;
    private static final int OWL_END = 9;

    private static String getSessionChronotype(SleepingSession s) {
        int sessionStart = s.getStartDateTime().getHour();
        int sessionEnd = s.getEndDateTime().getHour();
        if (sessionStart < LARK_START && sessionEnd < LARK_END) {
            return Chronotype.LARK.getDescription();
        }
        if (sessionStart >= OWL_START && sessionEnd >= OWL_END) {
            return Chronotype.OWL.getDescription();
        } else {
            return Chronotype.HUMMINGBIRD.getDescription();
        }
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
