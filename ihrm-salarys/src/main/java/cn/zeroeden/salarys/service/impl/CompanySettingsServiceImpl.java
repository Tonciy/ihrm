package cn.zeroeden.salarys.service.impl;

import cn.zeroeden.domain.salarys.CompanySettings;
import cn.zeroeden.salarys.dao.CompanySettingsDao;
import cn.zeroeden.salarys.service.CompanySettingsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanySettingsServiceImpl extends ServiceImpl<CompanySettingsDao, CompanySettings> implements CompanySettingsService {
	
    @Autowired
    private CompanySettingsDao companySettingsDao;

    /**
     * 根据id获取查询
     *
     * @param companyId 公司id
     * @return 具体数据
     */
    @Override
    public cn.zeroeden.domain.salarys.CompanySettings findById(String companyId) {
        LambdaQueryWrapper<CompanySettings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanySettings::getCompanyId, companyId);
        return companySettingsDao.selectOne(queryWrapper);
    }

    /**
     * 保存
     *
     * @param companySettings 公司配置
     */
    @Override
    public void saveByMy(cn.zeroeden.domain.salarys.CompanySettings companySettings) {
        companySettings.setIsSettings(1);
        companySettingsDao.insert(companySettings);
    }
}
