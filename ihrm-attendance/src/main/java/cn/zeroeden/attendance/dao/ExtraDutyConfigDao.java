package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.ExtraDutyConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExtraDutyConfigDao extends BaseMapper<ExtraDutyConfig> {

    /**
     * @return 根据公司和部门查询加班配置信息
     */
    ExtraDutyConfig findByCompanyIdAndDepartmentId(String companyId, String departmentId);
}
