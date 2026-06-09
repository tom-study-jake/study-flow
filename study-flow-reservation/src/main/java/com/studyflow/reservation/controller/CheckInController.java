package com.studyflow.reservation.controller;

import com.studyflow.dto.Result;
import com.studyflow.reservation.service.ICheckInService;
import com.studyflow.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckInController {
    private final ICheckInService checkInService;

    // 签到
    @RequestMapping("/check-In")
    public Result<String> checkIn(
            @RequestBody Map<String, Long> params) {
        Long userId = UserHolder.getUserId();
        Long reservationId = params.get("reservationId");
        checkInService.checkIn(reservationId, userId);
        return Result.ok("签到成功");
    }
    // 签退
    @RequestMapping("/check-Out")
    public Result<String> checkOut(
            @RequestBody Map<String, Long> params) {
        Long userId = UserHolder.getUserId();
        Long reservationId = params.get("reservationId");
        checkInService.checkOut(reservationId, userId);
        return Result.ok("签退成功，感谢使用");
    }
}
