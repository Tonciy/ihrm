package cn.zeroeden.salarys.dao;

import cn.zeroeden.domain.salarys.Settings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingsDao extends BaseMapper<Settings> {
}
