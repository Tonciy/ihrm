package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.UserCompanyJobs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问接口
 */
@Mapper
public interface UserCompanyJobsDao extends BaseMapper<UserCompanyJobs> {
    @Select("select * from em_user_company_jobs where user_id = #{userId} ")
    UserCompanyJobs findByUserId(String userId);
}