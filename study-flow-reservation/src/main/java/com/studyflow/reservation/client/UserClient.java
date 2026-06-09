package com.studyflow.reservation.client;

import com.studyflow.dto.CreditVO;
import com.studyflow.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("study-flow-user")
public interface UserClient {

    // 查询用户信用分
    @GetMapping("/api/users/internal/credit/{userId}")
    Result<CreditVO> getCreditByUserId(@PathVariable("userId") Long userId);

    // 扣信用分（爽约时用）
    @PostMapping("/api/users/internal/credit/deduct")
    Result<Integer> deductCredit(@RequestParam("userId") Long userId, @RequestParam("score") int score);

    // 签到信用更新（重置连续爽约）
    @PostMapping("/api/users/internal/credit/checkin")
    Result<Void> checkInUpdate(@RequestParam("userId") Long userId);
}
