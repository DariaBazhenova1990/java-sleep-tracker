package ru.yandex.practicum.sleeptracker.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeIdentifierTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepingSession> LARK_TYPE = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 21:00", FORMATTER),
                    LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("02.10.25 21:30", FORMATTER),
                    LocalDateTime.parse("03.10.25 05:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("04.10.25 01:00", FORMATTER),
                    LocalDateTime.parse("04.10.25 11:00", FORMATTER),
                    SleepQuality.BAD));

    private static final List<SleepingSession> NO_SESSIONS = List.of();

    private static final List<SleepingSession> OWL_TYPE = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 21:00", FORMATTER),
                    LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("02.10.25 23:30", FORMATTER),
                    LocalDateTime.parse("03.10.25 10:00", FORMATTER),
                    SleepQuality.BAD),
            new SleepingSession(
                    LocalDateTime.parse("04.10.25 00:00", FORMATTER),
                    LocalDateTime.parse("04.10.25 11:00", FORMATTER),
                    SleepQuality.BAD));

    private static final List<SleepingSession> HUMMINGBIRD_TYPE = List.of(
            new SleepingSession(
                    LocalDateTime.parse("31.10.25 21:00", FORMATTER),
                    LocalDateTime.parse("01.11.25 10:00", FORMATTER),
                    SleepQuality.GOOD));

    private static final List<SleepingSession> MULTIPLE_TYPES = List.of(
            new SleepingSession(
                    LocalDateTime.parse("01.10.25 21:00", FORMATTER),
                    LocalDateTime.parse("02.10.25 06:00", FORMATTER),
                    SleepQuality.GOOD),
            new SleepingSession(
                    LocalDateTime.parse("04.10.25 01:00", FORMATTER),
                    LocalDateTime.parse("04.10.25 11:00", FORMATTER),
                    SleepQuality.BAD));

    static Stream<Arguments> chronotypeIdentifierProvider() {
        return Stream.of(
                Arguments.of(LARK_TYPE, Chronotype.LARK.getDescription()),
                Arguments.of(NO_SESSIONS, "хронотип не возможно определить"),
                Arguments.of(HUMMINGBIRD_TYPE, Chronotype.HUMMINGBIRD.getDescription()),
                Arguments.of(OWL_TYPE, Chronotype.OWL.getDescription()),
                Arguments.of(MULTIPLE_TYPES, Chronotype.HUMMINGBIRD.getDescription()));
    }

    @ParameterizedTest(name = "Тест {index}: Ожидаемый хронотип: {1}")
    @MethodSource("chronotypeIdentifierProvider")
    void testCountSleeplessNights(List<SleepingSession> testSessions, String expectedChronotype) {
        ChronotypeIdentifier chronotypeIdentifier = new ChronotypeIdentifier();
        SleepAnalysisResult result = chronotypeIdentifier.apply(testSessions);
        String chronotype = (String) result.getValue();
        assertEquals(expectedChronotype, chronotype, "Хронотип не равен ожидаемому значению.");
    }

}
