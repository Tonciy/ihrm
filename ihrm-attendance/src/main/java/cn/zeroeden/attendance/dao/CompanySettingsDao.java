package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.socialSecuritys.CompanySettings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanySettingsDao extends BaseMapper<CompanySettings> {
}
