package com.studyflow.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyflow.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
}
