package cn.zeroeden.attendance.service.impl;

import cn.zeroeden.attendance.dao.ArchiveMonthlyDao;
import cn.zeroeden.attendance.dao.ArchiveMonthlyInfoDao;
import cn.zeroeden.attendance.dao.AttendanceDao;
import cn.zeroeden.attendance.dao.UserDao;
import cn.zeroeden.attendance.service.ArchiveService;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthly;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArchiveServiceImpl  implements ArchiveService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;

    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;

    @Override
    public void saveArchive(String archiveDate, String companyId) {
        // 1. 获取所有员工数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getCompanyId, companyId);
        List<User> users = userDao.selectList(queryWrapper);
        // 2. 保存归档主表数据
        ArchiveMonthly archiveMonthly = new ArchiveMonthly();
        archiveMonthly.setId(idWorker.nextId() + "");
        archiveMonthly.setCompanyId(companyId);
        archiveMonthly.setArchiveYear(archiveDate.substring(0, 4));
        archiveMonthly.setArchiveMonth(archiveDate.substring(4));
        // 3. 保存归档明细表数据
        for (User user : users) {
            ArchiveMonthlyInfo info = new ArchiveMonthlyInfo(user);
            // 统计每个用户的考勤记录
            Map map = attendanceDao.statisByUser(user.getId(), archiveDate + "%");
            info.setStatisData(map);
            info.setId(idWorker.nextId()+"");
            info.setAtteArchiveMonthlyId(archiveMonthly.getId());
            archiveMonthlyInfoDao.insert(info);
        }
        // 总人数
        archiveMonthly.setTotalPeopleNum(users.size());
        archiveMonthly.setFullAttePeopleNum(users.size());
        archiveMonthly.setIsArchived(0);
        archiveMonthlyDao.insert(archiveMonthly);
    }

    @Override
    public List<ArchiveMonthly> findReportsByYear(String year, String companyId) {
        LambdaQueryWrapper<ArchiveMonthly> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveMonthly::getCompanyId, companyId);
        queryWrapper.eq(ArchiveMonthly::getArchiveYear, year);
        return archiveMonthlyDao.selectList(queryWrapper);
    }

    @Override
    public List<ArchiveMonthlyInfo> findMonthlyInfoByAmid(String id) {
        LambdaQueryWrapper<ArchiveMonthlyInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveMonthlyInfo::getAtteArchiveMonthlyId, id);
        return archiveMonthlyInfoDao.selectList(queryWrapper);
    }

    /**
     * 查询用户的已归档考勤数据
     *
     * @param userId    用户id
     * @param yearMonth 年月
     * @return 数据
     */
    @Override
    public ArchiveMonthlyInfo findUserArchiveDetail(String userId, String yearMonth) {
        LambdaQueryWrapper<ArchiveMonthlyInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveMonthlyInfo::getUserId, userId);
        queryWrapper.eq(ArchiveMonthlyInfo::getYearLeaveDays, yearMonth);
        return archiveMonthlyInfoDao.selectOne(queryWrapper);
    }
}
