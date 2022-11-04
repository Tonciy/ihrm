package cn.zeroeden.system.dao;

import cn.zeroeden.domain.system.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDao  extends BaseMapper<Permission> {
}
