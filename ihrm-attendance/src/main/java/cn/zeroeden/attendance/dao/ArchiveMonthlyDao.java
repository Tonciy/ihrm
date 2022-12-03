package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.ArchiveMonthly;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zero
 */
@Mapper
public interface ArchiveMonthlyDao extends BaseMapper<ArchiveMonthly> {


    /**
     * 查询某一年的归档列表
     * @param companyId
     * @param archiveYear
     * @return
     */
    List<ArchiveMonthly> findByCompanyIdAndArchiveYear(String companyId, String archiveYear);


}
