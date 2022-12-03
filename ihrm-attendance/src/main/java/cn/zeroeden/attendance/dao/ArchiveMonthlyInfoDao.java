package cn.zeroeden.attendance.dao;

import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ArchiveMonthlyInfoDao extends BaseMapper<ArchiveMonthlyInfo> {


    /**
     * 根据归档列表查询月归档详情
     *
     * @param atteArchiveMonthlyId
     * @return
     */
    List<ArchiveMonthlyInfo> findByAtteArchiveMonthlyId(String atteArchiveMonthlyId);
}