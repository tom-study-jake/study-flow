package com.studyflow.dto;

import lombok.Data;

import java.time.LocalTime;
//时间段
@Data
public class PeriodVO {
    private Long id;
    private Long roomId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotIndex;
}
