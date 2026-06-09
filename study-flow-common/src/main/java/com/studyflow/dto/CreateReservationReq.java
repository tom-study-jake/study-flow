package com.studyflow.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateReservationReq {
    private Long seatId;
    private Long periodId;
    private LocalDate date;
}
