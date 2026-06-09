package com.studyflow.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("tb_study_room")
public class StudyRoom {

    @TableId()//主键,配置文件里配置了全局主键生成策略这里不写了
    private Long id;
    private String name;
    private Integer floor;
    private String area;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String description;
    private Integer status;

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill= FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
