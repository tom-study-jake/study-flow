package com.studyflow.reservation.service;

import com.studyflow.dto.RoomVO;

import java.util.List;

public interface IRoomService {
    //获取教室列表,null查所有
    List<RoomVO> getRoomList(Integer floor, String area);

    RoomVO getRoomDetail(Long id);

    List<com.studyflow.dto.PeriodVO> getRoomPeriods(Long id);
}
