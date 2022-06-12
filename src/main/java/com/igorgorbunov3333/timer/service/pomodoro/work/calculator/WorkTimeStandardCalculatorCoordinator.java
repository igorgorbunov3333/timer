package com.igorgorbunov3333.timer.service.pomodoro.work.calculator;

import com.igorgorbunov3333.timer.model.dto.WorkingPomodorosPerformanceRateDto;
import com.igorgorbunov3333.timer.service.pomodoro.work.calculator.enums.CalculationPeriod;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class WorkTimeStandardCalculatorCoordinator {

    private final Map<CalculationPeriod, WorkTimeStandardCalculator> calculatorsByPeriod;

    @Autowired
    public WorkTimeStandardCalculatorCoordinator(List<WorkTimeStandardCalculator> calculators) {
        this.calculatorsByPeriod = calculators.stream()
                .collect(Collectors.toMap(WorkTimeStandardCalculator::period, Function.identity()));
    }

    public WorkingPomodorosPerformanceRateDto calculate(CalculationPeriod period) {
        WorkTimeStandardCalculator calculator = calculatorsByPeriod.get(period);
        if (calculator == null) {
            throw new IllegalArgumentException(String.format("Wrong period: %s", period.name())); //TODO: handle this exception
        }

        return calculator.calculate();
    }

}
