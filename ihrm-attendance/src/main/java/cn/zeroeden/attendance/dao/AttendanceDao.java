package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.Attendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface AttendanceDao extends BaseMapper<Attendance> {

    /**
     * 统计某员工某年月各种考勤情况
     * @param id 用户id
     * @param s 年月
     * @return 具体数据
     */
    @Select("SELECT COUNT(*) at0,\n" +
            "       COUNT(CASE WHEN adt_statu=1 THEN 1 END) at1,\n" +
            "       COUNT(CASE WHEN adt_statu=2 THEN 1 END) at2,\n" +
            "       COUNT(CASE WHEN adt_statu=3 THEN 1 END) at3,\n" +
            "       COUNT(CASE WHEN adt_statu=4 THEN 1 END) at4,\n" +
            "       COUNT(CASE WHEN adt_statu=8 THEN 1 END) at8,\n" +
            "       COUNT(CASE WHEN adt_statu=17 THEN 1 END) at17\n" +
            "       FROM atte_attendance WHERE  user_id=#{id} AND DAY LIKE  #{s} ")
    Map statisByUser(String id, String s);
}
