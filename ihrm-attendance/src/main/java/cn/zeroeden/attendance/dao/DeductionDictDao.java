package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.DeductionDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeductionDictDao  extends BaseMapper<DeductionDict> {


    /**
     * 根据公司和部门查询扣款配置信息
     * @param companyId
     * @param departmentId
     * @return     *
     */
    List<DeductionDict> findByCompanyIdAndDepartmentId(String companyId, String departmentId);


    /**
     * 查询扣款配置
     * @param companyId
     * @param departmentId
     * @param dedTypeCode
     * @return
     */
    DeductionDict findByCompanyIdAndDepartmentIdAndDedTypeCode(String companyId, String departmentId, String dedTypeCode);


}
