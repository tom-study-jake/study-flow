package com.studyflow.dto;

import lombok.Data;

@Data
public class SeatVO {
    private Long id;
    private Long roomId;
    private Integer rowNum;
    private Integer colNum;
    private String seatNo;
    private Integer seatType;
    private Boolean occupied;
}
