package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalTime;
//时段
@Data
@TableName("tb_time_period")
public class TimePeriod {
    @TableId
    private Long id;

    private Long roomId;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer status;

    private Integer slotIndex;
}
