package cn.zeroeden.salarys.service.impl;


import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import cn.zeroeden.domain.salarys.SalaryArchive;
import cn.zeroeden.domain.salarys.SalaryArchiveDetail;
import cn.zeroeden.domain.salarys.Settings;
import cn.zeroeden.domain.salarys.UserSalary;
import cn.zeroeden.domain.socialSecuritys.ArchiveDetail;
import cn.zeroeden.salarys.dao.ArchiveDao;
import cn.zeroeden.salarys.dao.ArchiveDetailDao;
import cn.zeroeden.salarys.dao.UserSalaryDao;
import cn.zeroeden.salarys.feign.FeignClientService;
import cn.zeroeden.salarys.service.ArchiveService;
import cn.zeroeden.salarys.service.SalaryService;
import cn.zeroeden.salarys.service.SettingsService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveDao, SalaryArchive> implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private ArchiveDetailDao archiveDetailDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private FeignClientService feignClientService;
    @Autowired
    private UserSalaryDao userSalaryDao;

    @Autowired
    private SettingsService settingsService;

    /**
     * 根据企业id和年月查询归档主表数据
     *
     * @param yearMonth 年月
     * @param companyId 企业id
     * @return 归档主表数据记录
     */
    @Override
    public SalaryArchive findSalaryArchive(String yearMonth, String companyId) {
        LambdaQueryWrapper<SalaryArchive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalaryArchive::getCompanyId, companyId);
        queryWrapper.eq(SalaryArchive::getYearsMonth, yearMonth);
        return archiveDao.selectOne(queryWrapper);
    }

    /**
     * 根据归档id查询所有归档明细记录
     *
     * @param id 归档id
     * @return 所有归档明细记录
     */
    @Override
    public List<SalaryArchiveDetail> findSalaryArchiveDetail(String id) {
        LambdaQueryWrapper<SalaryArchiveDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalaryArchiveDetail::getArchiveId, id);
        return archiveDetailDao.selectList(queryWrapper);
    }

    @Override
    public List<SalaryArchiveDetail> getReports(String yearMonth, String companyId) {
        List<SalaryArchiveDetail> list = new ArrayList<>();
        // 查询当前企业的福利津贴
        Settings settings = settingsService.findById(companyId);
        // 查询所有用户
        List<Map> all = userSalaryDao.findAll(companyId);
        for (Map map : all) {
            // 构造SalaryArchiveDetail
            SalaryArchiveDetail detail = new SalaryArchiveDetail();
            detail.setUser(map); // 构造用户数据
            // 获取用户的社保数据
            ArchiveDetail socialInfo = feignClientService.getSocialInfo(detail.getUserId(), yearMonth);
            detail.setSocialInfo(socialInfo);
            // 获取用户的考勤数据
            ArchiveMonthlyInfo atteInfo = feignClientService.getAtteInfo(detail.getUserId(), yearMonth);
            detail.setAtteInfo(atteInfo);
            // 获取用户的薪资
            UserSalary userSalary = salaryService.findUserSalary(detail.getUserId());
            detail.setUserSalary(userSalary);
            // 计算工资
            detail.calSalary(settings);
            list.add(detail);
        }
        return list;
    }
}
