package com.studyflow.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RoomVO {

    private Long id;
    private String name;
    private Integer floor;
    private String area;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String description;
}
