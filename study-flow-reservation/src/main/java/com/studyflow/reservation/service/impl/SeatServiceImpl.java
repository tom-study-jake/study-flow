package com.studyflow.reservation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studyflow.dto.AvailabilityVO;
import com.studyflow.dto.SeatVO;
import com.studyflow.entity.Seat;
import com.studyflow.reservation.mapper.SeatMapper;
import com.studyflow.reservation.service.ISeatService;
import com.studyflow.utils.RedisConstants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.studyflow.utils.RedisConstants.SEAT_OCCUPY;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements ISeatService {
    private final RedissonClient redissonClient;
    private final SeatMapper seatMapper;

    @Override
    public List<SeatVO> listSeats(Long roomId, LocalDate date, Long periodId) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<Seat> seats = seatMapper.selectList(
                new LambdaQueryWrapper<Seat>()
                        .eq(Seat::getRoomId, roomId)
                        .eq(Seat::getStatus, 0)
        );
        List<SeatVO> voList = BeanUtil.copyToList(seats, SeatVO.class);

        voList.forEach(vo -> {
            String occupyKey = String.format(SEAT_OCCUPY, vo.getId(), dateStr, periodId);
            vo.setOccupied(redissonClient.getBucket(occupyKey).get() != null);
        });

        return voList;
    }

    @Override
    public AvailabilityVO getAvailability(Long seatId, LocalDate date, Long periodId) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String occupyKey = String.format(SEAT_OCCUPY, seatId, dateStr, periodId);
        AvailabilityVO vo = new AvailabilityVO();
        vo.setSeatId(seatId);
        vo.setAvailable(redissonClient.getBucket(occupyKey).get() == null);
        return vo;
    }
}
