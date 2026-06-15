package com.studyflow.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studyflow.dto.LoginVO;
import com.studyflow.dto.RegisterReq;
import com.studyflow.entity.User;
import com.studyflow.entity.UserCredit;
import com.studyflow.user.mapper.UserCreditMapper;
import com.studyflow.user.mapper.UserMapper;
import com.studyflow.user.service.IUserService;
import com.studyflow.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final UserCreditMapper userCreditMapper;

    @Override
    public Long register(RegisterReq req) {
        // 检查手机号是否已注册
        User exist = userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getPhone, req.getPhone())
        );
        if (exist != null) {
            throw new RuntimeException("该手机号已注册");
        }

        // 保存用户（直接存原文密码，以后再加加密）
        User user = new User();
        user.setPhone(req.getPhone());
        user.setPassword(req.getPassword());
        user.setNickname(req.getNickname() != null ? req.getNickname() : "用户" + System.currentTimeMillis() % 10000);
        userMapper.insert(user);

        // 创建默认信用记录
        UserCredit credit = new UserCredit();
        credit.setUserId(user.getId());
        credit.setCreditScore(100);
        credit.setMissCount(0);
        credit.setMissStreak(0);
        credit.setIsBanned(0);
        userCreditMapper.insert(credit);

        return user.getId();
    }

    @Override
    public LoginVO login(String phone, String password) {
        // 查用户
        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getPhone, phone)
        );
        if (user == null) {
            throw new RuntimeException("手机号未注册");
        }

        // 校验密码（明文对比）
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        // 生成 token
        String token = JwtUtils.generateToken(user.getId());

        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setToken(token);
        vo.setNickname(user.getNickname());
        return vo;
    }
}
