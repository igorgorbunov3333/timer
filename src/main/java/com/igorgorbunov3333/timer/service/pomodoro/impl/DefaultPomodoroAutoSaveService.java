package com.igorgorbunov3333.timer.service.pomodoro.impl;

import com.igorgorbunov3333.timer.model.dto.PeriodDto;
import com.igorgorbunov3333.timer.model.dto.PomodoroDto;
import com.igorgorbunov3333.timer.model.entity.Pomodoro;
import com.igorgorbunov3333.timer.repository.PomodoroRepository;
import com.igorgorbunov3333.timer.service.mapper.PomodoroMapper;
import com.igorgorbunov3333.timer.service.pomodoro.PomodoroAutoSaveService;
import com.igorgorbunov3333.timer.service.pomodoro.PomodoroFreeSlotFinderService;
import com.igorgorbunov3333.timer.service.pomodoro.synchronization.PomodoroSynchronizationScheduler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class DefaultPomodoroAutoSaveService implements PomodoroAutoSaveService {

    private final PomodoroRepository pomodoroRepository;
    private final PomodoroFreeSlotFinderService pomodoroFreeSlotFinderService;
    private final PomodoroSynchronizationScheduler pomodoroSynchronizationScheduler;
    private final PomodoroMapper pomodoroMapper;

    @Override
    @Transactional
    public PomodoroDto save() {
        PeriodDto latestFreeSlot = pomodoroFreeSlotFinderService.findFreeSlotInCurrentDay();
        Pomodoro pomodoroToSave = buildPomodoro(latestFreeSlot);

        Pomodoro savedPomodoro = pomodoroRepository.save(pomodoroToSave);
        pomodoroSynchronizationScheduler.addRemovalJob();
        return pomodoroMapper.mapToDto(savedPomodoro);
    }

    private Pomodoro buildPomodoro(PeriodDto latestFreeSlot) {
        ZonedDateTime pomodoroStartTime = latestFreeSlot.getStart()
                .truncatedTo(ChronoUnit.SECONDS)
                .atZone(ZoneId.systemDefault());
        ZonedDateTime pomodoroEndTime = latestFreeSlot.getEnd()
                .truncatedTo(ChronoUnit.SECONDS)
                .atZone(ZoneId.systemDefault());

        return new Pomodoro(null, pomodoroStartTime, pomodoroEndTime, true);
    }

}