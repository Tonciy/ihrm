package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问接口
 */
@Mapper
public interface UserCompanyPersonalDao extends BaseMapper<UserCompanyPersonal> {

    @Select("select * from em_user_company_personal where user_id = #userId")
    UserCompanyPersonal findByUserId(String userId);
}