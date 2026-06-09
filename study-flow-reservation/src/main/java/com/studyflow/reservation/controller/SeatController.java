package com.studyflow.reservation.controller;

import com.studyflow.dto.AvailabilityVO;
import com.studyflow.dto.Result;
import com.studyflow.dto.SeatVO;
import com.studyflow.reservation.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SeatController {
    private final ISeatService seatService;

    //查询某个自习室某天某时段的座位情况
    @GetMapping("/rooms/{roomId}/seats")
    public Result<List<SeatVO>> listSeats(
            @PathVariable Long roomId,
            @RequestParam LocalDate date,
            @RequestParam Long periodId) {
        return Result.ok(seatService.listSeats(roomId, date, periodId));
    }
    //查询能否预约
    @GetMapping("seats/{seatId}/availability")
    public Result<AvailabilityVO> avaliability(
            @PathVariable Long seatId,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
            @RequestParam Long periodId) {
        return Result.ok(seatService.getAvailability(seatId, date, periodId));
    }
}
