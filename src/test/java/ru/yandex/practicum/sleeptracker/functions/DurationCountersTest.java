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

class DurationCountersTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepingSession> MULTIPLE_SESSIONS = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 23:15", FORMATTER),
                    LocalDateTime.parse("02.10.25 07:30", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("02.10.25 23:50", FORMATTER),
                    LocalDateTime.parse("03.10.25 06:40", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("03.10.25 14:10", FORMATTER),
                    LocalDateTime.parse("03.10.25 15:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("03.10.25 23:40", FORMATTER),
                    LocalDateTime.parse("04.10.25 08:00", FORMATTER),
                    SleepQuality.NORMAL));

    private static final List<SleepingSession> NO_SESSIONS = List.of();

    private static final List<SleepingSession> SINGLE_SESSION = List.of(
            new SleepingSession(
                    LocalDateTime.parse("05.10.25 22:00", FORMATTER),
                    LocalDateTime.parse("06.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD));

    static Stream<Arguments> avgSessionsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 363.75),
                Arguments.of(NO_SESSIONS, 0.0),
                Arguments.of(SINGLE_SESSION, 480.0));
    }

    static Stream<Arguments> maxSessionsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 500),
                Arguments.of(NO_SESSIONS, 0),
                Arguments.of(SINGLE_SESSION, 480));
    }

    static Stream<Arguments> minSessionsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 50),
                Arguments.of(NO_SESSIONS, 0),
                Arguments.of(SINGLE_SESSION, 480));
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем результат: {1}")
    @MethodSource("avgSessionsProvider")
    void testCountAverageSessionDuration(List<SleepingSession> testSessions, double expectedCount) {
        AverageSessionDurationCounter counter = new AverageSessionDurationCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        double avgDuration = (double) result.getValue();
        assertEquals(expectedCount, avgDuration,
                "Средняя продолжительность сессии (в минутах) не равна ожидаемому значению.");
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем результат: {1}")
    @MethodSource("maxSessionsProvider")
    void testCountMaximumSessionDuration(List<SleepingSession> testSessions, long expectedCount) {
        MaximumSessionDurationCounter counter = new MaximumSessionDurationCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        long maxDuration = (long) result.getValue();
        assertEquals(expectedCount, maxDuration,
                "Максимальная продолжительность сессии (в минутах) не равна ожидаемому значению.");
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем результат: {1}")
    @MethodSource("minSessionsProvider")
    void testCountMinimumSessionDuration(List<SleepingSession> testSessions, long expectedCount) {
        MinimumSessionDurationCounter counter = new MinimumSessionDurationCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        long minDuration = (long) result.getValue();
        assertEquals(expectedCount, minDuration,
                "Минимальная продолжительность сессии (в минутах) не равна ожидаемому значению.");
    }
}
