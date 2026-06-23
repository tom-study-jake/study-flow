package com.studyflow.dto;

import lombok.Data;
// 登录请求
@Data
public class LoginReq {
    private String phone;
    private String password;
}
