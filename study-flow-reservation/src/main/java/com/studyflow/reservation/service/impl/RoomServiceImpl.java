package com.studyflow.reservation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.studyflow.dto.PeriodVO;
import com.studyflow.dto.RoomVO;
import com.studyflow.entity.StudyRoom;
import com.studyflow.entity.TimePeriod;
import com.studyflow.reservation.mapper.RoomMapper;
import com.studyflow.reservation.mapper.TimePeriodMapper;
import com.studyflow.reservation.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {
    private final RoomMapper roomMapper;
    private final TimePeriodMapper timePeriodMapper;

    @Override
    public List<RoomVO> getRoomList(Integer floor, String area) {
        List<StudyRoom> rooms = roomMapper.selectList(
                Wrappers.<StudyRoom>lambdaQuery()
                        .eq(StudyRoom::getStatus, 0)
                        .eq(floor != null, StudyRoom::getFloor, floor)
                        .eq(StringUtils.isNotBlank(area), StudyRoom::getArea, area)
        );
        return BeanUtil.copyToList(rooms, RoomVO.class);
    }

    @Override
    public RoomVO getRoomDetail(Long id) {
        StudyRoom room = roomMapper.selectById(id);
        if (room == null) {
            throw new RuntimeException("教室不存在");
        }
        return BeanUtil.copyProperties(room, RoomVO.class);
    }

    @Override
    public List<PeriodVO> getRoomPeriods(Long id) {
        List<TimePeriod> periods = timePeriodMapper.selectList(
                Wrappers.<TimePeriod>lambdaQuery()
                        .eq(TimePeriod::getRoomId, id)
                        .eq(TimePeriod::getStatus, 0)
                        .orderByAsc(TimePeriod::getSlotIndex)
        );
        return BeanUtil.copyToList(periods, PeriodVO.class);
    }
}
