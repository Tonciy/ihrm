package cn.zeroeden.social.dao;

import cn.zeroeden.domain.socialSecuritys.UserSocialSecurity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserSocialSecurityDao extends BaseMapper<UserSocialSecurity> {


    @Select("SELECT bu.id,\n" +
            "       bu.username,\n" +
            "       bu.mobile,\n" +
            "       bu.work_number  workNumber,\n" +
            "       bu.department_name departmentName,\n" +
            "       bu.time_of_entry timeOfEntry,\n" +
            "       bu.time_of_dimission leaveTime, \n" +
            "       ssuss.participating_in_the_city participatingInTheCity, \n" +
            "       ssuss.participating_in_the_city_id participatingInTheCityId,\n" +
            "       ssuss.provident_fund_city_id providentFundCityId, \n" +
            "       ssuss.provident_fund_city providentFundCity,\n" +
            "       ssuss.social_security_base socialSecurityBase,\n" +
            "       ssuss.provident_fund_base providentFundBase FROM bs_user bu LEFT JOIN ss_user_social_security ssuss ON bu.id=ssuss.user_id WHERE bu.company_id=#{companyId} limit #{start},#{pageSize} ")
    List<Map> findPage(String companyId, Integer start, Integer pageSize);


    @Select("SELECT count(1) FROM bs_user bu LEFT JOIN ss_user_social_security ssuss ON bu.id=ssuss.user_id WHERE bu.company_id=#{companyId} ")
    Long count(String companyId);
}
