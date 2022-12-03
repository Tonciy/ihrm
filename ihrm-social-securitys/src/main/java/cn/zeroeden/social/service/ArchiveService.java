package cn.zeroeden.social.service;

import cn.zeroeden.domain.socialSecuritys.Archive;
import cn.zeroeden.domain.socialSecuritys.ArchiveDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ArchiveService extends IService<Archive> {
    /**
     * 通过公司id和年月查询归档总结数据
     * @param companyId 公司id
     * @param yearMonth 年月
     * @return 此公司在某年月的归档总结数据
     */
    Archive findArchiveByCompanyIDAndYearMonth(String companyId, String yearMonth);

    /**
     * 根据归档总结对象的id去查询 社保具体明细
     * @param id 归档总结对象的id
     * @return 社保明细
     */
    List<ArchiveDetail> findAllDetailByArchiveId(String id);

    /**
     * 查询某公司某年月应该生成的社保报表数据
     * @param companyId 公司id
     * @param yearMonth 年月
     * @return 应该生成的社保报表数据
     */
    List<ArchiveDetail> getReports(String companyId, String yearMonth);

    /**
     * 对某个公司某年月的员工社保信息进行归档操作
     * @param companyId 公司id
     * @param yearMonth 年月
     */
    void archive(String companyId, String yearMonth);

    /**
     * 根据公司id和年份查询某公司的历史社保归档
     * @param companyId 公司id
     * @param year 年份
     * @return 此公司某年份每个月的历史社保归档集合
     */
    List<Archive> findArchiveByYearAndCompnayId(String companyId, String year);

    /**
     * 根据用户id和年月查询此用户在某年月的社保归档数据明细
     * @param userId 用户
     * @param yearMonth 年月
     * @return 用户的社保归档数据明细
     */
    ArchiveDetail findUserArchiveDetailByUserIdAndYearMonth(String userId, String yearMonth);
}
