package cn.zeroeden.system.dao;

import cn.zeroeden.domain.system.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description: 为角色-权限中间表提供基础操作接口
 */
@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermission> {
}
