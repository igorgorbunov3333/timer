//package com.igorgorbunov3333.timer.service.pomodoro.impl;
//
//import com.igorgorbunov3333.timer.config.properties.PomodoroProperties;
//import com.igorgorbunov3333.timer.model.dto.pomodoro.PomodoroDto;
//import com.igorgorbunov3333.timer.model.dto.tag.PomodoroTagDto;
//import com.igorgorbunov3333.timer.model.entity.dayoff.DayOff;
//import com.igorgorbunov3333.timer.repository.DayOffRepository;
//import com.igorgorbunov3333.timer.service.pomodoro.report.calculator.work.impl.CurrentWeekWorkTimeStandardCalculator;
//import com.igorgorbunov3333.timer.service.util.CurrentTimeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CurrentWeekWorkTimeStandardReportCalculatorTest {
//
//    private static final LocalDate WEEK_START_DAY = LocalDate.of(2022, 6, 6);
//    private static final String POMODORO_WORK_TAG = "work";
//
//    @InjectMocks
//    private CurrentWeekWorkTimeStandardCalculator testee;
//
//    @Mock
//    private PomodoroProperties pomodoroProperties;
//    @Mock
//    private DayOffRepository dayOffRepository;
//    @Mock
//    private CurrentTimeService currentTimeService;
//
//    @Test
//    void calculate_WhenEndOfWeekAndNoDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtEndOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(-55);
//    }
//
//    @Test
//    void calculate_WhenStartOfWeekAndNoDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtStartOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(-11);
//    }
//
//    @Test
//    void calculate_WhenMiddleOfWeekAndNoDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeInMiddleOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(-33);
//    }
//
//    @Test
//    void calculate_WhenEndOfWeekAndAllDaysAreDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtEndOfWeek();
//        mockLocalDayOffs(5, startDateTime);
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(0);
//    }
//
//    @Test
//    void calculate_WhenStartOfWeekAndAllDaysAreDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtStartOfWeek();
//        mockLocalDayOffs(5, startDateTime);
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(0);
//    }
//
//    @Test
//    void calculate_WhenMiddleOfWeekAndAllDaysAreDayOffsAndNoWorkedPomodoro_ThenCalculate() {
//        LocalDateTime startTime = mockCurrentTimeInMiddleOfWeek();
//        mockLocalDayOffs(5, startTime);
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(List.of());
//
//        assertThat(actual).isEqualTo(0);
//    }
//
//    @Test
//    void calculate_WhenEndOfWeekAndWorkedPomodoroMoreThanSetForStandard_ThenCalculate() {
//        LocalDateTime startTime = mockCurrentTimeAtEndOfWeek();
//        mockLocalDayOffs(1, startTime);
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(61));
//
//        assertThat(actual).isEqualTo(17);
//    }
//
//    @Test
//    void calculate_WhenStartOfWeekAndWorkedPomodoroMoreThanSetForStandard_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtStartOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(13));
//
//        assertThat(actual).isEqualTo(2);
//    }
//
//    @Test
//    void calculate_WhenMiddleOfWeekAndWorkedPomodoroMoreThanSetForStandard_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeInMiddleOfWeek();
//        mockLocalDayOffs(1, startDateTime);
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(35));
//
//        assertThat(actual).isEqualTo(13);
//    }
//
//    @Test
//    void calculate_WhenEndOfWeekAndWorkedPomodoroLessThanSetForStandard_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtEndOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(50));
//
//        assertThat(actual).isEqualTo(-5);
//    }
//
//    @Test
//    void calculate_WhenStartOfWeekAndWorkedPomodoroLessThanSetForStandard_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeAtStartOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(8));
//
//        assertThat(actual).isEqualTo(-3);
//    }
//
//    @Test
//    void calculate_WhenMiddleOfWeekAndWorkedPomodoroLessThanSetForStandard_ThenCalculate() {
//        LocalDateTime startDateTime = mockCurrentTimeInMiddleOfWeek();
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startDateTime.toLocalDate())).thenReturn(List.of());
//        mockPomodoroProperties();
//
//        int actual = testee.calculate(mockPomodoros(5));
//
//        assertThat(actual).isEqualTo(-28);
//    }
//
//    private void mockLocalDayOffs(long amountInWeek, LocalDateTime startTime) {
//        List<DayOff> dayOffs = new ArrayList<>();
//        for (LocalDate startDate = WEEK_START_DAY; startDate.isBefore(WEEK_START_DAY.plusDays(amountInWeek)); startDate = startDate.plusDays(1L)) {
//            DayOff dayOff = mock(DayOff.class);
//            when(dayOff.getDay()).thenReturn(startDate);
//            dayOffs.add(dayOff);
//        }
//
//        when(dayOffRepository.findByDayGreaterThanEqualOrderByDay(startTime.toLocalDate())).thenReturn(dayOffs);
//    }
//
//    private List<PomodoroDto> mockPomodoros(int amount) {
//        List<PomodoroDto> result = new ArrayList<>();
//        for (int i = 1; i <= amount; i++) {
//            PomodoroDto pomodoroDto = mock(PomodoroDto.class);
//
//            PomodoroTagDto tag = mock(PomodoroTagDto.class);
//            when(tag.getName()).thenReturn(POMODORO_WORK_TAG);
//
//            when(pomodoroDto.getTags()).thenReturn(List.of(tag));
//            result.add(pomodoroDto);
//        }
//        return result;
//    }
//
//    private LocalDateTime mockCurrentTimeAtEndOfWeek() {
//        LocalDateTime startDateTime = WEEK_START_DAY.plusDays(6L).atStartOfDay();
//        when(currentTimeService.getCurrentDateTime()).thenReturn(startDateTime);
//        return startDateTime.minusDays(6L);
//    }
//
//    private LocalDateTime mockCurrentTimeAtStartOfWeek() {
//        LocalDateTime startDateTime = WEEK_START_DAY.atStartOfDay();
//        when(currentTimeService.getCurrentDateTime()).thenReturn(WEEK_START_DAY.atStartOfDay());
//        return startDateTime;
//    }
//
//    private LocalDateTime mockCurrentTimeInMiddleOfWeek() {
//        LocalDateTime startDateTime = WEEK_START_DAY.plusDays(2L).atStartOfDay();
//        when(currentTimeService.getCurrentDateTime()).thenReturn(WEEK_START_DAY.plusDays(2L).atStartOfDay());
//        return startDateTime.minusDays(2L);
//    }
//
//    private void mockPomodoroProperties() {
//        PomodoroProperties.Standard standard = mock(PomodoroProperties.Standard.class);
//        when(standard.getWork()).thenReturn(11);
//
//        when(pomodoroProperties.getStandard()).thenReturn(standard);
//
//        PomodoroProperties.Tag tag = mock(PomodoroProperties.Tag.class);
//        PomodoroProperties.TagDescription work = mock(PomodoroProperties.TagDescription.class);
//        when(tag.getWork()).thenReturn(work);
//        when(work.getName()).thenReturn("work");
//
//        when(pomodoroProperties.getTag()).thenReturn(tag);
//    }
//
//}
