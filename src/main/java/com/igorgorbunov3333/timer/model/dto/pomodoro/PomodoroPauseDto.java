package com.igorgorbunov3333.timer.model.dto.pomodoro;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PomodoroPauseDto {

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime endTime;

    @Override
    public String toString() {
        return "Pomodoro {" +
                ", startTime=" + startTime.toLocalDateTime() +
                ", endTime=" + endTime.toLocalDateTime() +
                '}';
    }

}
