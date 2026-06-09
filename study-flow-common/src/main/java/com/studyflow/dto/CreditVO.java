package com.studyflow.dto;

import lombok.Data;

@Data
public class CreditVO {
    private Long userId;
    private Integer creditScore;
    private Integer missCount;
    private Boolean isBanned;
}
