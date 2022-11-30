package cn.zeroeden.social.service;

import cn.zeroeden.domain.socialSecuritys.CompanySettings;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CompanySettingsService extends IService<CompanySettings> {

    /**
     * 根据id查询企业是否设置过社保
     * @param companyId 企业id
     * @return 载体
     */
    CompanySettings findById(String companyId);

    /**
     * 为某个企业某个年月做社保
     * @param companySettings
     */
    void mySave(CompanySettings companySettings);
}
