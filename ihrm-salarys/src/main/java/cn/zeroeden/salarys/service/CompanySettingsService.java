package cn.zeroeden.salarys.service;

import cn.zeroeden.domain.salarys.CompanySettings;

public interface CompanySettingsService {
    /**
     * 根据id获取查询
     * @param companyId 公司id
     * @return 具体数据
     */
     CompanySettings findById(String companyId) ;

    /**
     * 保存
     * @param companySettings 公司配置
     */
     void saveByMy(CompanySettings companySettings);

}
