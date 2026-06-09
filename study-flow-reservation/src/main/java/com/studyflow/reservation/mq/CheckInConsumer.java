package com.studyflow.reservation.mq;

import com.studyflow.reservation.config.RabbitMqConfig;
import com.studyflow.entity.Reservation;
import com.studyflow.reservation.client.UserClient;
import com.studyflow.reservation.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CheckInConsumer {

    private final ReservationMapper reservationMapper;
    private final UserClient userClient;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_CHECKIN)
    @Transactional
    public void handleCheckIn(String message) {
        Long reservationId = Long.valueOf(message);
        System.out.println("签到成功消息，预约ID: " + reservationId);

        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            System.out.println("预约记录不存在，跳过");
            return;
        }

        // 通过 Feign 调用用户服务，更新信用分（消除连续爽约记录）
        userClient.checkInUpdate(reservation.getUserId());
        System.out.println("用户 " + reservation.getUserId() + " 信用分已更新（Feign调用）");
    }
}
