package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.EmployeeResignation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问接口
 */
@Mapper
public interface EmployeeResignationDao extends BaseMapper<EmployeeResignation> {

    @Select("select * from em_resignation where user_id = #uid  ")
    EmployeeResignation findByUserId(String uid);
}