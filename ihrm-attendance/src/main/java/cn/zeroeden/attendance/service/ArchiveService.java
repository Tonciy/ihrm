package cn.zeroeden.attendance.service;

import cn.zeroeden.domain.attendance.entity.ArchiveMonthly;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;

import java.util.List;

public interface ArchiveService {


    /**
     * 保存月报表归档数据
     * @param archiveDate 年月
     * @param companyId 公司id
     */
    void saveArchive(String archiveDate, String companyId);

    /**
     * 查询考勤归档历史列表
     * @param year 年份
     * @param companyId 公司id
     * @return 数据
     */
    List<ArchiveMonthly> findReportsByYear(String year, String companyId);

    /**
     * 查询某个月的归档详情
     * @param id 主表id
     * @return 具体考勤数据
     */
    List<ArchiveMonthlyInfo> findMonthlyInfoByAmid(String id);

    /**
     * 查询用户的已归档考勤数据
     * @param userId 用户id
     * @param yearMonth 年月
     * @return 数据
     */
    ArchiveMonthlyInfo findUserArchiveDetail(String userId, String yearMonth);
}
