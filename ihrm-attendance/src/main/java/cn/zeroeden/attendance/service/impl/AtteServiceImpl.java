package cn.zeroeden.attendance.service.impl;

import cn.zeroeden.attendance.dao.AttendanceDao;
import cn.zeroeden.attendance.dao.CompanySettingsDao;
import cn.zeroeden.attendance.dao.UserDao;
import cn.zeroeden.attendance.service.AtteService;
import cn.zeroeden.domain.attendance.bo.AtteItemBO;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import cn.zeroeden.domain.attendance.entity.Attendance;
import cn.zeroeden.domain.socialSecuritys.CompanySettings;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.utils.DateUtil;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AtteServiceImpl extends ServiceImpl<AttendanceDao, Attendance> implements AtteService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanySettingsDao companySettingsDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 获取员工考勤数据列表
     * 1. 考勤月
     * 2. 员工数据分页
     * 3. 查询每个员工考勤信息
     *
     * @param companyId 公司id
     * @param page      当前页
     * @param pageSize  一页展示数量
     * @return 员工考勤数据列表
     */
    @Override
    public Map getAtteData(String companyId, int page, int pageSize)  throws Exception {
        // 1. 考勤月
        LambdaQueryWrapper<CompanySettings> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CompanySettings::getCompanyId, companyId);
        CompanySettings settings = companySettingsDao.selectOne(queryWrapper1);
        String dataMonth = settings.getDataMonth();
        // 2. 分页查询员工数据
        IPage<User> myPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(User::getCompanyId, companyId);
        myPage=userDao.selectPage(myPage, queryWrapper2);
        long totoalNumber = myPage.getTotal();
        List<User> users = myPage.getRecords();
        List<AtteItemBO> list = new ArrayList<>();
        // 对每个员工去查询构造他的考勤记录
        for (User user : users) {
            AtteItemBO bo = new AtteItemBO();
            // 拷贝基础员工信息
            BeanUtils.copyProperties(user, bo);
            // 用来装这个月的考勤记录
            ArrayList<Attendance> attendances = new ArrayList<>();
            // 获取当前月所有的天数
            String[] days = DateUtil.getDaysByYearMonth(dataMonth);
            // 循环每天，查询此员工的每天考勤记录
            for (String day : days) {
                LambdaQueryWrapper<Attendance> queryWrapper3 = new LambdaQueryWrapper<>();
                queryWrapper3.eq(Attendance::getUserId, user.getId());
                queryWrapper3.eq(Attendance::getDay, day);
                Attendance attendance = attendanceDao.selectOne(queryWrapper3);
                if(attendance == null){
                    // 旷工
                    attendance = new Attendance();
                    attendance.setAdtStatu(2);
                    attendance.setUserId(user.getId());
                    attendance.setDay(day);
                }
                attendances.add(attendance);
            }
            bo.setAttendanceRecord(attendances);
            list.add(bo);
        }
        // 构造返回数据
        HashMap<String, Object> map = new HashMap<>();
        // 存储数据的分页对象
        PageResult<AtteItemBO> pr = new PageResult<>(totoalNumber, list);
        map.put("data", pr);
        // 待处理的考勤数量
        map.put("tobeTaskCount", 0);
        // 当前考勤月份
        map.put("monthOfReport", Integer.parseInt(dataMonth.substring(4)));
        return map;
    }

    @Override
    public void editAtte(Attendance attendance) {
        LambdaQueryWrapper<Attendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attendance::getUserId, attendance.getUserId());
        queryWrapper.eq(Attendance::getDay, attendance.getDay());
        Attendance target = attendanceDao.selectOne(queryWrapper);
        if(target == null){
            // 插入
            attendance.setId(idWorker.nextId() + "");
            attendanceDao.insert(attendance);
        }else{
            // 更新
            attendance.setId(target.getId());
            attendanceDao.updateById(attendance);

        }

    }

    @Override
    public List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getCompanyId, companyId);
        List<User> users = userDao.selectList(queryWrapper);
        List<ArchiveMonthlyInfo> list = new ArrayList<>();
        for (User user : users) {
            ArchiveMonthlyInfo info = new ArchiveMonthlyInfo(user);
            // 统计每个用户的考勤记录
            Map map = attendanceDao.statisByUser(user.getId(), atteDate + "%");
            info.setStatisData(map);
            list.add(info);
        }
        return list;
    }
}
