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
}
