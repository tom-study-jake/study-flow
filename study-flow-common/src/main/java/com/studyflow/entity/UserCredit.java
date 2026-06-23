package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
//信用分
@TableName("tb_user_credit")
@Data
public class UserCredit {
    @TableId
    private Long id;
    private Long userId;
    private Integer creditScore;
    private Integer missCount;
    private Integer missStreak;//连续次数
    private Integer isBanned;//0 正常 1 封禁
    private LocalDateTime banUntil;
    private LocalDateTime updateTime;
}
