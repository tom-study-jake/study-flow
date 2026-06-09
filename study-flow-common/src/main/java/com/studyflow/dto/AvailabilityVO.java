package com.studyflow.dto;

import lombok.Data;

@Data
public class AvailabilityVO {

    private Long seatId;
    private String seatNo;
    private Boolean available;  // true = 可预约 false = 已被占
}
