package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.EmployeeTransferPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问接口
 */
@Mapper
public interface TransferPositionDao extends BaseMapper<EmployeeTransferPosition> {

    @Select("select * from em_transferposition where user_id = #{uid} ")
    EmployeeTransferPosition findByUserId(String uid);
}