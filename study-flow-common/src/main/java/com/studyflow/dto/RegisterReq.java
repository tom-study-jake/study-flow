package com.studyflow.dto;

import lombok.Data;

@Data
public class RegisterReq {
    private String phone;
    private String password;
    private String nickname;
}
