package com.igorgorbunov3333.timer.service.commandline;

import com.igorgorbunov3333.timer.model.dto.PomodoroDto;
import com.igorgorbunov3333.timer.model.dto.engine.PomodoroActionInfoDto;
import com.igorgorbunov3333.timer.service.commandline.impl.DefaultPrinterService;
import com.igorgorbunov3333.timer.service.exception.DataPersistingException;
import com.igorgorbunov3333.timer.service.pomodoro.PomodoroPeriodService;
import com.igorgorbunov3333.timer.service.pomodoro.PomodoroService;
import com.igorgorbunov3333.timer.service.pomodoro.engine.PomodoroEngine;
import com.igorgorbunov3333.timer.service.pomodoro.engine.PomodoroEngineService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
@AllArgsConstructor
public class CommandLine {

    private static final String INVALID_INPUT = "Invalid input, please try again";

    private final PomodoroService pomodoroService;
    private final PomodoroPeriodService pomodoroPeriodService;
    private final PomodoroEngine pomodoroEngine;
    private final PomodoroEngineService pomodoroEngineService;
    private final PrinterService printerService;

    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        printerService.printFeaturesList();
        while (true) {
            String text = sc.nextLine();
            try {
                gotoChoice(text);
            } catch (Exception e) {
                System.out.println(INVALID_INPUT);
            }
            if (text.equals("exit")) {
                break;
            }
        }
        sc.close();
    }

    @SneakyThrows
    private void gotoChoice(String input) {
        if (input.equals("1")) {
            PomodoroActionInfoDto pomodoroActionInfoDto = pomodoroEngineService.startPomodoro();
            if (!pomodoroActionInfoDto.isDoneSuccessfully()) {
                System.out.println(pomodoroActionInfoDto.getFailureMessage());
            } else {
                System.out.println("Pomodoro has started");
                printerService.printFirstThreeFirstPomodoroSecondsDuration();
            }
        } else if (input.equals("2")) {
            PomodoroActionInfoDto pomodoroActionInfoDto = pomodoroEngineService.stopPomodoro();
            if (!pomodoroActionInfoDto.isDoneSuccessfully()) {
                System.out.println(pomodoroActionInfoDto.getFailureMessage());
            } else {
                System.out.println(DefaultPrinterService.MESSAGE_POMODORO_SAVED + pomodoroActionInfoDto.getValue());
                printerService.getAndPrintDailyPomodoros();
            }
        } else if (input.equals("3")) {
            String pomodoroCurrentDuration = pomodoroEngineService.getPomodoroCurrentDuration();
            System.out.println(pomodoroCurrentDuration);
        } else if (input.equals("4")) {
            System.out.println(pomodoroService.getPomodorosInDay());
        } else if (input.equals("5")) {
            printerService.getAndPrintDailyPomodoros();
        } else if (input.equals("6")) {
            printerService.printPomodorosForLastMonth();
        } else if (input.equals("help")) {
            printerService.printFeaturesList();
        } else if (input.startsWith("remove")) {
            char[] inputChars = input.toCharArray();
            if (inputChars.length == "remove".length()) {
                List<PomodoroDto> dailyPomodoros = pomodoroService.getPomodorosInDayExtended();
                if (dailyPomodoros.isEmpty()) {
                    System.out.println("Unable to remove latest pomodoro as no daily pomodoros");
                    return;
                }
                Long removedPomodoroId;
                try {
                    removedPomodoroId = pomodoroService.removeLatest();
                } catch (DataPersistingException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                if (removedPomodoroId != null) {
                    System.out.println("Pomodoro with id " + removedPomodoroId + " successfully removed");
                }
                return;
            }
            int index = "remove ".length();
            if (inputChars[index - 1] != ' ') {
                System.out.println("Incorrect input \"" + input + "\". \"remove\" and id should be separated with \" \"");
                return;
            }
            String pomodoroIdArgument = getArgumentString(input, inputChars, index);
            Long pomodoroId = Long.valueOf(pomodoroIdArgument);
            pomodoroService.removePomodoro(pomodoroId);
        } else if (input.startsWith("save")) {
            PomodoroDto savedPomodoro;
            try {
                savedPomodoro = pomodoroService.save();
            } catch (DataPersistingException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println(DefaultPrinterService.MESSAGE_POMODORO_SAVED + savedPomodoro);
            printerService.getAndPrintDailyPomodoros();
        } else if (input.equals("week")) {
            Map<DayOfWeek, List<PomodoroDto>> weeklyPomodoros = pomodoroPeriodService.getCurrentWeekPomodoros();
            if (weeklyPomodoros.isEmpty()) {
                System.out.println(DefaultPrinterService.MESSAGE_POMODORO_SAVED);
            }
            for (Map.Entry<DayOfWeek, List<PomodoroDto>> entry : weeklyPomodoros.entrySet()) {
                System.out.println();
                System.out.println(entry.getKey().toString());
                List<PomodoroDto> dailyPomodoros = entry.getValue();
                printerService.printDailyPomodoros(dailyPomodoros, false);
            }
        } else if (input.equals("pause")) {
            pomodoroEngine.pausePomodoro();
            System.out.println("Pomodoro paused!");
        } else if (input.equals("resume")) {
            pomodoroEngine.resumePomodoro();
        } else {
            System.out.println(INVALID_INPUT);
        }
    }

    private String getArgumentString(String input, char[] inputChars, int index) {
        char[] pomodoroIdInString = new char[input.length() - index];
        for (int i = index, j = 0; i < inputChars.length; i++, j++) {
            pomodoroIdInString[j] = inputChars[i];
        }
        return new String(pomodoroIdInString);
    }

}
