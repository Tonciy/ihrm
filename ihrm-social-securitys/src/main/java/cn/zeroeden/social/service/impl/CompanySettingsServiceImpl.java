package cn.zeroeden.social.service.impl;

import cn.zeroeden.domain.socialSecuritys.CompanySettings;
import cn.zeroeden.social.dao.CompanySettingsDao;
import cn.zeroeden.social.service.CompanySettingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanySettingsServiceImpl extends ServiceImpl<CompanySettingsDao, CompanySettings> implements CompanySettingsService {


    @Autowired
    private CompanySettingsDao companySettingsDao;
    @Override
    public CompanySettings findById(String companyId) {
        CompanySettings cs = companySettingsDao.selectById(companyId);
        return cs;
    }

    @Override
    public void mySave(CompanySettings companySettings) {
        // 设置当前月
        companySettings.setIsSettings(1);
        companySettingsDao.insert(companySettings);
    }
}
