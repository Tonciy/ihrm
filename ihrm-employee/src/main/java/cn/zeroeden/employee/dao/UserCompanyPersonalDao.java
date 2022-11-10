package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import cn.zeroeden.domain.employee.response.EmployeeReportResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据访问接口
 */
@Mapper
public interface UserCompanyPersonalDao extends BaseMapper<UserCompanyPersonal> {

    @Select("select * from em_user_company_personal where user_id = #{userId}")
    UserCompanyPersonal findByUserId(String userId);


    @Select("select * from em_user_company_personal a left join em_resignation b on a.user_id  = b.user_id " +
            " where a.company_id = #{companyId} and ( a.time_of_entry like concat(#{month}, '%') or b.resignation_time like concat(#{month}, '%')) ")
    List<EmployeeReportResult> findByReport(String companyId, String month);
}