package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.DayOffConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DayOffConfigDao extends BaseMapper<DayOffConfig> {



    /**
     * @param companyId
     * @param departmentId
     * @return 根据公司和部门查询扣款配置信息
     *
     */
    DayOffConfig findByCompanyIdAndDepartmentId(String companyId, String departmentId);
}
