package com.studyflow.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.studyflow.reservation.config.RabbitMqConfig;
import com.studyflow.entity.CheckIn;
import com.studyflow.entity.Reservation;
import com.studyflow.reservation.mapper.CheckInMapper;
import com.studyflow.reservation.mapper.ReservationMapper;
import com.studyflow.reservation.service.ICheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements ICheckInService {
    private final ReservationMapper reservationMapper;
    private final CheckInMapper checkInMapper;
    private final RabbitTemplate rabbitTemplate;

    // 签到
    @Override
    public void checkIn(Long reservationId, Long userId) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservationId == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new RuntimeException("这不是你的");
        }
        if (reservation.getStatus() != 0) {
            throw new RuntimeException("没到签到时机");
        }
        // 签到
        reservation.setCheckInTime(LocalDateTime.now());
        reservation.setStatus(1);
        reservationMapper.updateById(reservation);
        //插入签到记录
        CheckIn checkIn = new CheckIn();
        checkIn.setReservationId(reservationId);
        checkIn.setUserId(userId);
        checkIn.setSeatId(reservation.getSeatId());
        checkIn.setCheckInTime(LocalDateTime.now());
        checkInMapper.insert(checkIn);

        // 发 MQ 消息：签到成功，更新信用分
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_CHECKIN,
                RabbitMqConfig.RK_CHECKIN,
                reservationId.toString());
    }

    // 签退
    @Override
    public void checkOut(Long reservationId, Long userId) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservationId == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new RuntimeException("这不是你的");
        }
        if (reservation.getStatus() != 1) {
            throw new RuntimeException("没到签退时机");
        }
        reservation.setStatus(2);
        reservationMapper.updateById(reservation);
        // 插入签退记录
        CheckIn checkIn = checkInMapper.selectOne(new LambdaQueryWrapper<CheckIn>()
                .eq(CheckIn::getReservationId, reservationId)
                .eq(CheckIn::getUserId, userId));

        if (checkIn != null) {
            checkIn.setCheckOutTime(LocalDateTime.now());
            // 计算使用时长（分钟）
            long minutes = Duration.between(checkIn.getCheckInTime(), LocalDateTime.now()).toMinutes();
            checkIn.setDurationMinutes((int) minutes);
            checkInMapper.updateById(checkIn);
        }
    }
}
