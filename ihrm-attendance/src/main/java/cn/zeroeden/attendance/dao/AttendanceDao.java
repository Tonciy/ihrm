package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.Attendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AttendanceDao extends BaseMapper<Attendance> {

}
