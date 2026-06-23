package com.studyflow.dto;

import lombok.Data;
import java.time.LocalDate;
// 预约请求
@Data
public class CreateReservationReq {
    private Long seatId;
    private Long periodId;
    private LocalDate date;
}
