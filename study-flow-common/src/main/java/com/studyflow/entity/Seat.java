package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_seat")
public class Seat {
    @TableId
    private Long id;

    private Long roomId;

    private Integer status;

    private Integer seatType;

    private String seatNo;

    private Integer rowNum;//行号

    private Integer colNum;//列号

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
