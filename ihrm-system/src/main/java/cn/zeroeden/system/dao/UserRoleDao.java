package cn.zeroeden.system.dao;

import cn.zeroeden.domain.system.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 为用户-角色中间表提供操作功能
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {
}
