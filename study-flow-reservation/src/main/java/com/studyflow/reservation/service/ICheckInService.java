package com.studyflow.reservation.service;

public interface ICheckInService {
    // 签到
    void checkIn(Long reservationId, Long userId);
    // 签退
    void checkOut(Long reservationId, Long userId);
}
