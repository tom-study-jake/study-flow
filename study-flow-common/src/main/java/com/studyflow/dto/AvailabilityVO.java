package com.studyflow.dto;

import lombok.Data;
//座位可用性
@Data
public class AvailabilityVO {

    private Long seatId;
    private String seatNo;
    private Boolean available;  // true = 可预约 false = 已被占
}
