package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
//签到记录
@Data
@TableName("tb_check_in")
public class CheckIn {
    @TableId
    private Long id;

    private Long userId;

    private Long reservationId;

    private Long seatId;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Integer durationMinutes;//使用时长（分）
}
