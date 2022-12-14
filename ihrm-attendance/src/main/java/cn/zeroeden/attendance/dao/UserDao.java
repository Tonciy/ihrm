package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<User> {

    public User findByMobile(String mobile);

    List<User> findByCompanyId(String companyId);
}
