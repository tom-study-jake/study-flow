package com.studyflow.dto;

import lombok.Data;

@Data
public class LoginVO {
    private Long userId;
    private String token;
    private String nickname;
}
