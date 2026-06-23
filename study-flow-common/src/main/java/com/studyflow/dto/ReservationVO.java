package com.studyflow.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
//预约详情返回
@Data
public class ReservationVO {
    private Long id;
    private Long userId;
    private Long roomId;
    private Long seatId;
    private String seatNo;//座位号
    private String roomName;
    private Long periodId;
    private LocalDate reserveDate;
    private Integer status;//0 待签到 1已签到 2已完成 3 爽约 4取消
    private String statusDesc;//状态描述
    private LocalDateTime expireTime;//预约过期时间
    private LocalDateTime createTime;
}
