package com.studyflow.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class PeriodVO {
    private Long id;
    private Long roomId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotIndex;
}
