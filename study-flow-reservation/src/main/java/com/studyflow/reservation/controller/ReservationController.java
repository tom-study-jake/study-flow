package com.studyflow.reservation.controller;

import com.studyflow.dto.CreateReservationReq;
import com.studyflow.dto.ReservationVO;
import com.studyflow.dto.Result;
import com.studyflow.reservation.service.IReservationService;
import com.studyflow.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    // 预约座位
    @PostMapping
    public Result<String> createReservation(
            @RequestBody CreateReservationReq req) {
        Long userId = UserHolder.getUserId();
        Long id = reservationService.createReservation(userId, req.getSeatId(), req.getPeriodId(), req.getDate());
        return Result.ok("预约成功，ID: " + id);
    }

    // 取消预约
    @DeleteMapping("/{id}")
    public Result<Void> cancelReservation(
            @PathVariable Long id) {
        Long userId = UserHolder.getUserId();
        reservationService.cancelReservation(id, userId);
        return Result.ok();
    }

    // 查询我的预约列表
    @GetMapping("/my")
    public Result<List<ReservationVO>> myReservations(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Long userId = UserHolder.getUserId();
        return Result.ok(reservationService.getUserReservations(userId, status, date));
    }

    // 预约详情
    @GetMapping("/{id}")
    public Result<ReservationVO> detail(@PathVariable Long id) {
        return Result.ok(reservationService.getReservationDetail(id));
    }
}
