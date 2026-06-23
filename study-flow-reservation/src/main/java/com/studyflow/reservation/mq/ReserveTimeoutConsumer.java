package com.studyflow.reservation.mq;

import com.studyflow.reservation.config.RabbitMqConfig;
import com.studyflow.entity.Reservation;
import com.studyflow.reservation.client.UserClient;
import com.studyflow.reservation.mapper.ReservationMapper;
import com.studyflow.utils.RedisConstants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static com.studyflow.utils.RedisConstants.SEAT_OCCUPY;
import static com.studyflow.utils.RedisConstants.USER_RESERVE;

@Component
@RequiredArgsConstructor
public class ReserveTimeoutConsumer {
    private final ReservationMapper reservationMapper;
    private final RedissonClient redissonClient;
    private final UserClient userClient;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_DLX)
    @Transactional
    public void handleTimeout(String message) {
        Long reservationId = Long.valueOf(message);
        System.out.println("收到超时消息，预约ID：" + reservationId);

        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            System.out.println("预约不存在");
            return;
        }
        if (reservation.getStatus() != 0) {
            System.out.println("预约状态已变更（status=" + reservation.getStatus() + "），跳过");
            return;
        }
        reservation.setStatus(3);
        reservationMapper.updateById(reservation);

        // 删除 Redis 占座标记
        String dateStr = reservation.getReserveDate()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String occupyKey = String.format(SEAT_OCCUPY, reservation.getSeatId(), dateStr, reservation.getPeriodId());
        redissonClient.getBucket(occupyKey).delete();

        String userKey = String.format(USER_RESERVE, reservation.getUserId(), dateStr, reservation.getPeriodId());
        redissonClient.getBucket(userKey).delete();

        // 通过 Feign 调用用户服务，扣信用分（爽约扣10分）
        userClient.deductCredit(reservation.getUserId(), 10);
        System.out.println("用户 " + reservation.getUserId() + " 信用分已扣减（Feign调用）");
    }
}