package cn.zeroeden.salarys.service;

import cn.zeroeden.domain.salarys.SalaryArchive;
import cn.zeroeden.domain.salarys.SalaryArchiveDetail;

import java.util.List;

public interface ArchiveService {


    /**
     * 根据企业id和年月查询归档主表数据
     * @param yearMonth 年月
     * @param companyId 企业id
     * @return 归档主表数据记录
     */
    SalaryArchive findSalaryArchive(String yearMonth, String companyId);

    /**
     * 根据归档id查询所有归档明细记录--
     * @param id 归档id
     * @return 所有归档明细记录
     */
    List<SalaryArchiveDetail> findSalaryArchiveDetail(String id);

    /**
     * 构造薪资报表数据
     * @param yearMonth
     * @param companyId
     * @return
     */
    List<SalaryArchiveDetail> getReports(String yearMonth, String companyId);
}
