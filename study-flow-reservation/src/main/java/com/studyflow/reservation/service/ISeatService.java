package com.studyflow.reservation.service;

import com.studyflow.dto.AvailabilityVO;
import com.studyflow.dto.SeatVO;

import java.time.LocalDate;
import java.util.List;

public interface ISeatService {
    List<SeatVO> listSeats(Long roomId, LocalDate date, Long periodId);

    AvailabilityVO getAvailability(Long seatId, LocalDate date, Long periodId);
}
