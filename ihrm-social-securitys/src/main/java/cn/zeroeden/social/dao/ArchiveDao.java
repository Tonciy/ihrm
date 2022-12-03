package cn.zeroeden.social.dao;

import cn.zeroeden.domain.socialSecuritys.Archive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArchiveDao extends BaseMapper<Archive> {
    @Select("select * from ss_archive where company_id = #{companyId} and years_month like concat(#{year}, '%')")
    List<Archive> findListByCompanyIdAndYear(String companyId, String year);
}
