package com.studyflow.dto;

import lombok.Data;
// 登录VO
@Data
public class LoginVO {
    private Long userId;
    private String token;
    private String nickname;
}
