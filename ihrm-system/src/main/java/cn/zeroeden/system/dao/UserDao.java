package cn.zeroeden.system.dao;

import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Zero
 * @time: 2022/11/2
 * @description:
 */

@Mapper
public interface UserDao extends BaseMapper<User> {
}
