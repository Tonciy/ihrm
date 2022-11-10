package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.EmployeePositive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问接口
 */
@Mapper
public interface PositiveDao extends BaseMapper<EmployeePositive> {
    @Select("select * from em_positive where user_id = #{uid} ")
    EmployeePositive findByUserId(String uid);
}