package com.studyflow.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyflow.entity.Seat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeatMapper extends BaseMapper<Seat> {
}
