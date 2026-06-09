package com.studyflow.user.controller;

import com.studyflow.dto.CreditVO;
import com.studyflow.dto.Result;
import com.studyflow.user.service.IUserCreditService;
import com.studyflow.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserCreditService userCreditService;

    // 查询本人的信用分（前端调用，需登录）
    @RequestMapping("/credit")
    public Result<CreditVO> credits() {
        Long userId = UserHolder.getUserId();
        return Result.ok(userCreditService.getCreditByUserId(userId));
    }

    // ====== 以下为内部 Feign 调用接口（不走登录校验） ======

    // 查询指定用户的信用分（Feign 调用）
    @GetMapping("/internal/credit/{userId}")
    public Result<CreditVO> getCreditByUserId(@PathVariable Long userId) {
        return Result.ok(userCreditService.getCreditByUserId(userId));
    }

    // 扣信用分（Feign 调用，爽约时用）
    @PostMapping("/internal/credit/deduct")
    public Result<Integer> deductCredit(@RequestParam Long userId, @RequestParam int score) {
        int remain = userCreditService.deductCredit(userId, score);
        return Result.ok("扣分成功", remain);
    }

    // 签到更新（Feign 调用，消除连续爽约记录）
    @PostMapping("/internal/credit/checkin")
    public Result<Void> checkInUpdate(@RequestParam Long userId) {
        userCreditService.checkInUpdate(userId);
        return Result.ok();
    }
}
