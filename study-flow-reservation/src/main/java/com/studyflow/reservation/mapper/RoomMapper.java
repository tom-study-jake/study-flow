package com.studyflow.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.studyflow.entity.StudyRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper extends BaseMapper<StudyRoom> {
}
