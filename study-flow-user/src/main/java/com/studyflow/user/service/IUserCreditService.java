package com.studyflow.user.service;

import com.studyflow.dto.CreditVO;

public interface IUserCreditService {
    CreditVO getCreditByUserId(Long userId);

    int deductCredit(Long userId, int score);

    /**
     * 签到成功后的信用更新：重置连续爽约、解封
     */
    void checkInUpdate(Long userId);
}
