package com.studyflow.reservation.mq;

import com.studyflow.reservation.config.RabbitMqConfig;
import com.studyflow.entity.Reservation;
import com.studyflow.reservation.mapper.ReservationMapper;
import com.studyflow.utils.RedisConstants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static com.studyflow.utils.RedisConstants.SEAT_OCCUPY;
import static com.studyflow.utils.RedisConstants.USER_RESERVE;

/**
 * 取消预约消费者
 * 异步释放 Redis 中的占座标记。
 */
@Component
@RequiredArgsConstructor
public class ReserveCancelConsumer {

    private final ReservationMapper reservationMapper;
    private final RedissonClient redissonClient;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_CANCEL)
    public void handleCancel(String message) {
        Long reservationId = Long.valueOf(message);
        System.out.println("取消预约消息，预约ID: " + reservationId);

        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            return;
        }

        // 删除 Redis 占座标记
        String dateStr = reservation.getReserveDate()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String occupyKey = String.format(SEAT_OCCUPY, reservation.getSeatId(), dateStr, reservation.getPeriodId());
        RBucket<Object> bucket = redissonClient.getBucket(occupyKey);
        bucket.delete();

        // 删除用户预约记录
        String userKey = String.format(USER_RESERVE, reservation.getUserId(), dateStr, reservation.getPeriodId());
        redissonClient.getBucket(userKey).delete();

        System.out.println("预约 " + reservationId + " 的 Redis 资源已释放");
    }
}
