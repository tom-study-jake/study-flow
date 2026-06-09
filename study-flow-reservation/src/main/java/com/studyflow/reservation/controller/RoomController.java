package com.studyflow.reservation.controller;

import com.studyflow.dto.PeriodVO;
import com.studyflow.dto.Result;
import com.studyflow.dto.RoomVO;
import com.studyflow.reservation.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final IRoomService roomService;

    //获取自习室列表，可以按楼层和区域筛选
    @GetMapping
    public Result<List<RoomVO>> getRooms(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) String area) {
        return Result.ok(roomService.getRoomList(floor, area));
    }
    //获取自习室详情
    @GetMapping("/{id}")
    public Result<RoomVO> getRoom(@PathVariable Long id) {
        return Result.ok(roomService.getRoomDetail(id));
    }
    //查询自习室可预约时段
    @GetMapping("/{id}/periods")
    public Result<List<PeriodVO>> getRoomPeriods(@PathVariable Long id) {
        return Result.ok(roomService.getRoomPeriods(id));
    }
}
