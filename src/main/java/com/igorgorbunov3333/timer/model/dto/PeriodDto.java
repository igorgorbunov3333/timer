package com.igorgorbunov3333.timer.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PeriodDto {

    private LocalDateTime start;
    private LocalDateTime end;

}
