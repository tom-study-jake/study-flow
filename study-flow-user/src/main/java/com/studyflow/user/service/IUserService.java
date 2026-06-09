package com.studyflow.user.service;

import com.studyflow.dto.LoginVO;
import com.studyflow.dto.RegisterReq;

public interface IUserService {

    /**
     * 注册
     * @return 用户ID
     */
    Long register(RegisterReq req);

    /**
     * 登录
     * @return 包含 token 的 VO，失败返回 null
     */
    LoginVO login(String phone, String password);
}
