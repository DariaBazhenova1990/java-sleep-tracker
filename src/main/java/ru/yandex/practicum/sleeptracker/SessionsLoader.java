package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SessionsLoader {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final int START_SESSION_INDEX = 0;
    private static final int END_SESSION_INDEX = 1;
    private static final int QUALITY_INDEX = 2;
    private static final int SESSION_FIELDS_COUNT = 3;

    public static List<SleepingSession> loadSessions(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .map(SessionsLoader::parseLine)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] sessionRecord = line.split(";");

        if (sessionRecord.length != SESSION_FIELDS_COUNT) {
            return null;
        }

        try {
            LocalDateTime startDateTime = LocalDateTime.parse(sessionRecord[START_SESSION_INDEX], FORMATTER);
            LocalDateTime endDateTime = LocalDateTime.parse(sessionRecord[END_SESSION_INDEX], FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(sessionRecord[QUALITY_INDEX].toUpperCase());
            return new SleepingSession(startDateTime, endDateTime, quality);
        } catch (Exception e) {
            System.out.println("Ошибка: дата и/или время в строке '" + line
                    + "' не соответствует формату 'dd.MM.yy HH:mm'.");
            return null;
        }
    }

}
