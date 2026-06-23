package com.studyflow.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studyflow.dto.CreditVO;
import com.studyflow.entity.UserCredit;
import com.studyflow.user.mapper.UserCreditMapper;
import com.studyflow.user.service.IUserCreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreditServiceImpl implements IUserCreditService {
    private final UserCreditMapper userCreditMapper;

    @Override
    public CreditVO getCreditByUserId(Long userId) {
        UserCredit userCredit = userCreditMapper.selectOne(
                new LambdaQueryWrapper<UserCredit>()
                        .eq(UserCredit::getUserId, userId)
        );
        if (userCredit == null) {
            // 自动创建默认信用记录
            userCredit = new UserCredit();
            userCredit.setUserId(userId);
            userCredit.setCreditScore(100);
            userCredit.setMissCount(0);
            userCredit.setMissStreak(0);
            userCredit.setIsBanned(0);
            userCreditMapper.insert(userCredit);
        }
        CreditVO vo = BeanUtil.copyProperties(userCredit, CreditVO.class);
        vo.setIsBanned(userCredit.getIsBanned() == 1);
        return vo;
    }

    @Override
    public void checkInUpdate(Long userId) {
        UserCredit userCredit = userCreditMapper.selectOne(
                new LambdaQueryWrapper<UserCredit>()
                        .eq(UserCredit::getUserId, userId)
        );
        if (userCredit == null) {
            return;
        }
        // 连续爽约次数归零
        userCredit.setMissStreak(0);
        // 如果被封禁，解除封禁
        if (userCredit.getIsBanned() == 1) {
            userCredit.setIsBanned(0);
            userCredit.setBanUntil(null);
        }
        userCreditMapper.updateById(userCredit);
    }

    @Override
    public int deductCredit(Long userId, int score) {
        UserCredit userCredit = userCreditMapper.selectOne(
                new LambdaQueryWrapper<UserCredit>()
                        .eq(UserCredit::getUserId, userId)
        );
        if (userCredit == null) {
            return -1;
        }
        //扣分（下线0）
        userCredit.setCreditScore(Math.max(0, userCredit.getCreditScore() - score));
        userCredit.setMissCount(userCredit.getMissCount() + 1);
        userCredit.setMissStreak(userCredit.getMissStreak() + 1);

        if (userCredit.getMissStreak() >= 3) {
            userCredit.setIsBanned(1);
        }
        userCreditMapper.updateById(userCredit);
        return userCredit.getCreditScore();
    }
}
