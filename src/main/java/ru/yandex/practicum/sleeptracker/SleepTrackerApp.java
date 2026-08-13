package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.AverageSessionDurationCounter;
import ru.yandex.practicum.sleeptracker.functions.MaximumSessionDurationCounter;
import ru.yandex.practicum.sleeptracker.functions.MinimumSessionDurationCounter;
import ru.yandex.practicum.sleeptracker.functions.TotalSessionsCounter;
import ru.yandex.practicum.sleeptracker.functions.BadQualitySessionsCounter;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analysisFunctions = new ArrayList<>();

    public SleepTrackerApp() {
        analysisFunctions.add(new TotalSessionsCounter());
        analysisFunctions.add(new MinimumSessionDurationCounter());
        analysisFunctions.add(new MaximumSessionDurationCounter());
        analysisFunctions.add(new AverageSessionDurationCounter());
        analysisFunctions.add(new BadQualitySessionsCounter());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Укажите путь к файлу с логом сна!");
            return;
        }

        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = SessionsLoader.loadSessions(filePath);
            app.analysisFunctions.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Ошибка во время чтения файла с логом сна: " + e.getMessage());
        }
    }
}
