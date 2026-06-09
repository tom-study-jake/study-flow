package com.studyflow.user.controller;

import com.studyflow.dto.LoginReq;
import com.studyflow.dto.LoginVO;
import com.studyflow.dto.RegisterReq;
import com.studyflow.dto.Result;
import com.studyflow.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    // 注册
    @PostMapping("/register")
    public Result<Long> register(@RequestBody RegisterReq req) {
        Long userId = userService.register(req);
        return Result.ok("注册成功", userId);
    }

    // 登录
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginReq req) {
        LoginVO vo = userService.login(req.getPhone(), req.getPassword());
        return Result.ok("登录成功", vo);
    }
}
