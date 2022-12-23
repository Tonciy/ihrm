package cn.zeroeden.salarys.service.impl;

import cn.zeroeden.domain.salarys.Settings;
import cn.zeroeden.salarys.dao.SettingsDao;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsDao, Settings> implements cn.zeroeden.salarys.service.SettingsService {
    @Autowired
    private SettingsDao settingsDao;

    /**
     * 根据id获取企业设置
     *
     * @param companyId 企业id
     * @return 企业设置
     */
    @Override
    public Settings findById(String companyId) {
        LambdaQueryWrapper<Settings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Settings::getCompanyId, companyId);
        return settingsDao.selectOne(queryWrapper);
    }

    /**
     * 保存配置
     *
     * @param settings
     */
    @Override
    public void saveByMy(Settings settings) {
        settingsDao.insert(settings);
    }
}
