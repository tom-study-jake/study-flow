package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("tb_reservation")
@Data
public class Reservation {

    @TableId
    private Long id;

    private Long userId;

    private Long seatId;

    private Long roomId;

    private Long periodId;

    private LocalDate reserveDate;//预约日期

    private Integer status;//0未签到 1已签到 2已完成 3超时 4取消

    private LocalDateTime checkInTime;//签到时间

    private LocalDateTime expireTime;//失效时间

    private LocalDateTime cancelTime;//取消时间

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
