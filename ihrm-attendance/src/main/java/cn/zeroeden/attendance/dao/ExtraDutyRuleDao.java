package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.ExtraDutyRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExtraDutyRuleDao extends BaseMapper<ExtraDutyRule> {

    /**
     *
     *  根据公司和部门查询加班规则配置信息
     * @param extraDutyConfigId
     * @return
     */
    List<ExtraDutyRule> findByExtraDutyConfigId(String extraDutyConfigId);


    /**
     * 删除配置id的规则
     * @param extraDutyConfigId
     * @return
     */
    Integer deleteByExtraDutyConfigId(String extraDutyConfigId);


    /**
     * @param companyId
     * @param departmentId
     * @return
     */
    List<ExtraDutyRule> findByCompanyIdAndDepartmentId(String companyId, String departmentId);
}
