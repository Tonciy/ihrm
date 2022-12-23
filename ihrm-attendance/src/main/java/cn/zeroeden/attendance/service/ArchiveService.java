package cn.zeroeden.attendance.service;

public interface ArchiveService {


    /**
     * 保存月报表归档数据
     * @param archiveDate 年月
     * @param companyId 公司id
     */
    void saveArchive(String archiveDate, String companyId);
}
