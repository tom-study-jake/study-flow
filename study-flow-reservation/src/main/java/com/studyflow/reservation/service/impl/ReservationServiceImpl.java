package com.studyflow.reservation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studyflow.reservation.config.RabbitMqConfig;
import com.studyflow.dto.ReservationVO;
import com.studyflow.entity.Reservation;
import com.studyflow.entity.Seat;
import com.studyflow.entity.StudyRoom;
import com.studyflow.reservation.mapper.ReservationMapper;
import com.studyflow.reservation.mapper.RoomMapper;
import com.studyflow.reservation.mapper.SeatMapper;
import com.studyflow.reservation.service.IReservationService;
import com.studyflow.reservation.websocket.SeatWebSocketHandler;
import com.studyflow.utils.RedisConstants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.studyflow.utils.RedisConstants.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final ReservationMapper reservationMapper;
    private final SeatMapper seatMapper;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;
    private final RoomMapper roomMapper;
    private final SeatWebSocketHandler seatWebSocketHandler;

    //创建预约
    @Override
    public Long createReservation(Long userId, Long seatId, Long periodId, LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String occupyKey = String.format(SEAT_OCCUPY, seatId, dateStr, periodId);
        String userKey = String.format(USER_RESERVE, userId, dateStr, periodId);
        //查座位
        Seat seat = seatMapper.selectById(seatId);
        if (seat == null) {
            throw new RuntimeException("座位不存在");
        }

        String stockKey = String.format(SEAT_STOCK, seat.getRoomId(), dateStr, periodId);
        //执行Lua脚本占座
        //先检查用户是否已占座
        RBucket<Object> userBucket = redissonClient.getBucket(userKey);
        if (userBucket.isExists()) {
            throw new RuntimeException("您在这个时段已经有预约的了");
        }
        //原子占座：SETNX 如果不存在才设置
        RBucket<Object> occupyBucket = redissonClient.getBucket(occupyKey);
        boolean setSuccess = occupyBucket.trySet(String.valueOf(userId));
        if (!setSuccess) {
            throw new RuntimeException("这个位置已经被预约");
        }
        occupyBucket.expire(Duration.ofSeconds(RESERVE_TTL));
        //记录用户已占座
        userBucket.set(String.valueOf(userId));
        userBucket.expire(Duration.ofSeconds(RESERVE_TTL));
        //扣库存
        RBucket<Object> stockBucket = redissonClient.getBucket(stockKey);
        String stockVal = (String) stockBucket.get();
        if (stockVal == null) {
            //库存未设置，默认10
            stockBucket.set("10");
            stockVal = "10";
        }
        int stock = Integer.parseInt(stockVal);
        if (stock <= 0) {
            //回滚：删占座标记
            occupyBucket.delete();
            userBucket.delete();
            throw new RuntimeException("这个位置的剩余座位数不够");
        }
        stockBucket.set(String.valueOf(stock - 1));
        //记录
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSeatId(seatId);
        reservation.setRoomId(seat.getRoomId());
        reservation.setPeriodId(periodId);
        reservation.setReserveDate(date);
        reservation.setStatus(0);  // 0-待签到
        reservation.setExpireTime(LocalDateTime.now().plusMinutes(15));  // 15分钟后过期

        reservationMapper.insert(reservation);
        String pushMsg = String.format(
                "{\"seatId\":%d,\"status\":\"occupied\",\"userId\":%d}",
                seatId, userId);
        seatWebSocketHandler.broadcastToRoom(seat.getRoomId(), pushMsg);

        return reservation.getId();
    }

    // 取消预约
    @Override
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new RuntimeException("这不是你的预约");
        }
        if (reservation.getStatus() != 0) {
            throw new RuntimeException("不能取消");
        }
        reservation.setStatus(4);
        reservation.setCancelTime(LocalDateTime.now());
        reservationMapper.updateById(reservation);
        //通过mq，删除redis占座标记
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_RESERVE,
                RabbitMqConfig.RK_CANCEL,
                reservationId.toString());
    }

    //查预约记录
    @Override
    public List<ReservationVO> getUserReservations(Long userId, Integer status, LocalDate date) {
        List<Reservation> reservations = reservationMapper.selectList(
                Wrappers.<Reservation>lambdaQuery()
                        .eq(Reservation::getUserId, userId)
                        .eq(status != null, Reservation::getStatus, status)
                        .eq(date != null, Reservation::getReserveDate, date)
                        .orderByDesc(Reservation::getCreateTime)
        );
        return BeanUtil.copyToList(reservations, ReservationVO.class);
    }

    // 查预约详情
    @Override
    public ReservationVO getReservationDetail(Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            return null;
        }
        ReservationVO vo = BeanUtil.copyProperties(reservation, ReservationVO.class);
        switch (reservation.getStatus()) {
            case 0:
                vo.setStatusDesc("待签到");
                break;
            case 1:
                vo.setStatusDesc("已签到");
                break;
            case 2:
                vo.setStatusDesc("已完成");
                break;
            case 3:
                vo.setStatusDesc("爽约");
                break;
            case 4:
                vo.setStatusDesc("已取消");
                break;
        }
        Seat seat = seatMapper.selectById(reservation.getSeatId());
        if (seat != null) {
            vo.setSeatNo(seat.getSeatNo());
        }
        StudyRoom room = roomMapper.selectById(reservation.getRoomId());
        if (room != null) {
            vo.setRoomName(room.getName());
        }
        return vo;
    }
}
