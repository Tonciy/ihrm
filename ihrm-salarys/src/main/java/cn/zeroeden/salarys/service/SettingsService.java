package cn.zeroeden.salarys.service;

import cn.zeroeden.domain.salarys.Settings;

public interface SettingsService {

    /**
     * 根据id获取企业设置
     *
     * @param companyId 企业id
     * @return 企业设置
     */
    public Settings findById(String companyId) ;

    /**
     * 保存配置
     */
    public void saveByMy(Settings settings);

}
