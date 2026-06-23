package com.studyflow.dto;

import lombok.Data;
//信用积分
@Data
public class CreditVO {
    private Long userId;
    private Integer creditScore;
    private Integer missCount;
    private Boolean isBanned;
}
