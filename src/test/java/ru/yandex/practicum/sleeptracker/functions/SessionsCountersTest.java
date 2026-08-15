package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SessionsCountersTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepingSession> MULTIPLE_SESSIONS = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 23:15", FORMATTER),
                    LocalDateTime.parse("02.10.25 07:30", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("02.10.25 23:15", FORMATTER),
                    LocalDateTime.parse("03.10.25 07:30", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("03.10.25 23:15", FORMATTER),
                    LocalDateTime.parse("04.10.25 07:30", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("03.10.25 23:15", FORMATTER),
                    LocalDateTime.parse("04.10.25 07:30", FORMATTER),
                    SleepQuality.NORMAL));

    private static final List<SleepingSession> NO_SESSIONS = List.of();

    private static final List<SleepingSession> SINGLE_SESSION = List.of(
            new SleepingSession(
                    LocalDateTime.parse("05.10.25 22:00", FORMATTER),
                    LocalDateTime.parse("06.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD));

    static Stream<Arguments> totalSessionsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 4),
                Arguments.of(NO_SESSIONS, 0),
                Arguments.of(SINGLE_SESSION, 1));
    }

    static Stream<Arguments> badQualitySessionsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 2),
                Arguments.of(NO_SESSIONS, 0),
                Arguments.of(SINGLE_SESSION, 0));
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем сессий: {1}")
    @MethodSource("totalSessionsProvider")
    void testCountTotalSessions(List<SleepingSession> testSessions, int expectedCount) {
        TotalSessionsCounter counter = new TotalSessionsCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        int totalSessions = (int) result.getValue();
        assertEquals(expectedCount, totalSessions,
                "Общее количество сессий сна не равно ожидаемому значению.");
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем плохих сессий: {1}")
    @MethodSource("badQualitySessionsProvider")
    void testCountBadQualitySessions(List<SleepingSession> testSessions, int expectedCount) {
        BadQualitySessionsCounter counter = new BadQualitySessionsCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        long badSessions = (long) result.getValue();
        assertEquals(expectedCount, badSessions,
                "Количество сессий сна с плохим качеством не равно ожидаемому значению.");
    }
}
