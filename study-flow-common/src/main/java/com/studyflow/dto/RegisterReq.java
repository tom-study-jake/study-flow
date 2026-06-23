package com.studyflow.dto;

import lombok.Data;
// 注册请求
@Data
public class RegisterReq {
    private String phone;
    private String password;
    private String nickname;
}
