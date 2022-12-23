package cn.zeroeden.salarys.dao;

import cn.zeroeden.domain.salarys.UserSalary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserSalaryDao extends BaseMapper<UserSalary> {

	@Select(
			value="select bu.id,bu.username,bu.mobile,bu.work_number workNumber," +
					"bu.in_service_status inServiceStatus,bu.department_name departmentName,bu.department_id departmentId,bu.time_of_entry timeOfEntry ," +
					"bu.form_of_employment formOfEmployment ,sauss.current_basic_salary currentBasicSalary,sauss.current_post_wage currentPostWage from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE bu.company_id = #{companyId}" +
                    " limit #{start}, #{pageSize}"
	)
    List<Map> findPage(String companyId, int start, int pageSize);

    @Select("count(*) from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE bu.company_id = #{companyId}")
    int findPageGetTotal(String companyId);
}
