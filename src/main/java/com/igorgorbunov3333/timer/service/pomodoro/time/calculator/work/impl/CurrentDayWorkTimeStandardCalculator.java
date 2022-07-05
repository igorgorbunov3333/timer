package com.igorgorbunov3333.timer.service.pomodoro.time.calculator.work.impl;

import com.igorgorbunov3333.timer.config.properties.PomodoroProperties;
import com.igorgorbunov3333.timer.model.dto.pomodoro.PomodoroDto;
import com.igorgorbunov3333.timer.repository.DayOffRepository;
import com.igorgorbunov3333.timer.service.pomodoro.time.calculator.enums.PomodoroPeriod;
import com.igorgorbunov3333.timer.service.pomodoro.time.calculator.work.WorkTimeStandardCalculator;
import com.igorgorbunov3333.timer.service.util.CurrentTimeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Getter
@Service
@AllArgsConstructor
public class CurrentDayWorkTimeStandardCalculator implements WorkTimeStandardCalculator {

    private final CurrentTimeService currentTimeService;
    private final PomodoroProperties pomodoroProperties;
    private final DayOffRepository dayOffRepository;

    @Override
    public int calculate(List<PomodoroDto> pomodoro) {
        LocalDate today = currentTimeService.getCurrentDateTime().toLocalDate();

        return calculate(today, pomodoro);
    }

    @Override
    public PomodoroPeriod period() {
        return PomodoroPeriod.DAY;
    }

}
