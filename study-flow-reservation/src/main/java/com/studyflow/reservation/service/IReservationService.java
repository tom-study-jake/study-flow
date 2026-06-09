package com.studyflow.reservation.service;

import com.studyflow.dto.ReservationVO;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    // 创建预约
    Long createReservation(Long userId, Long seatId, Long periodId, LocalDate date);
    // 取消预约
    void cancelReservation(Long reservationId, Long userId);
    //查预约记录
    List<ReservationVO> getUserReservations(Long userId, Integer status, LocalDate date);
    // 查预约详情
    ReservationVO getReservationDetail(Long id);
}
