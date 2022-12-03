package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.LeaveConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveConfigDao extends BaseMapper<LeaveConfig> {



    /**
     * @param companyId
     * @param departmentId
     * @return 根据公司和部门查询考请假配置信息
     *
     */
    List<LeaveConfig> findByCompanyIdAndDepartmentId(String companyId, String departmentId);


    /**
     * @return 根据公司、部门和请假类型查询考请假配置信息
     */
    LeaveConfig findByCompanyIdAndDepartmentIdAndLeaveType(String companyId, String departmentId,String leaveType);

}
