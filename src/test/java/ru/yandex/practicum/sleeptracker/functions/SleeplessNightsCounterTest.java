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

class SleeplessNightsCounterTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepingSession> MULTIPLE_SESSIONS = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 23:00", FORMATTER),
                    LocalDateTime.parse("02.10.25 08:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("02.10.25 19:00", FORMATTER),
                    LocalDateTime.parse("03.10.25 05:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("04.10.25 01:00", FORMATTER),
                    LocalDateTime.parse("04.10.25 11:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("05.10.25 02:00", FORMATTER),
                    LocalDateTime.parse("05.10.25 05:00", FORMATTER),
                    SleepQuality.NORMAL),
            new SleepingSession(
                    LocalDateTime.parse("05.10.25 17:00", FORMATTER),
                    LocalDateTime.parse("05.10.25 23:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("06.10.25 07:00", FORMATTER),
                    LocalDateTime.parse("06.10.25 11:00", FORMATTER),
                    SleepQuality.BAD));


    private static final List<SleepingSession> NO_SESSIONS = List.of();

    private static final List<SleepingSession> CROSS_MONTH_SESSION = List.of(
            new SleepingSession(
                    LocalDateTime.parse("31.10.25 22:00", FORMATTER),
                    LocalDateTime.parse("01.11.25 06:00", FORMATTER),
                    SleepQuality.GOOD));

    private static final List<SleepingSession> SESSION_BEFORE_MIDDAY = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 09:00", FORMATTER),
                    LocalDateTime.parse("01.10.25 11:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 22:00", FORMATTER),
                    LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("03.10.25 00:00", FORMATTER),
                    LocalDateTime.parse("03.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD));

    static Stream<Arguments> sleeplessNightsProvider() {
        return Stream.of(
                Arguments.of(MULTIPLE_SESSIONS, 1),
                Arguments.of(NO_SESSIONS, 0),
                Arguments.of(CROSS_MONTH_SESSION, 0),
                Arguments.of(SESSION_BEFORE_MIDDAY, 1));
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаем бессонных ночей: {1}")
    @MethodSource("sleeplessNightsProvider")
    void testCountTotalSessions(List<SleepingSession> testSessions, long expectedCount) {
        SleeplessNightsCounter counter = new SleeplessNightsCounter();
        SleepAnalysisResult result = counter.apply(testSessions);
        long sleeplessNights = (long) result.getValue();
        assertEquals(expectedCount, sleeplessNights,
                "Количество бессонных ночей не равно ожидаемому значению.");
    }

}
