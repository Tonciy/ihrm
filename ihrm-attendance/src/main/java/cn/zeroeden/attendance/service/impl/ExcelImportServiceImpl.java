package cn.zeroeden.attendance.service.impl;

import cn.zeroeden.attendance.dao.AttendanceConfigDao;
import cn.zeroeden.attendance.dao.AttendanceDao;
import cn.zeroeden.attendance.dao.UserDao;
import cn.zeroeden.attendance.service.ExcelImportService;
import cn.zeroeden.domain.attendance.entity.Attendance;
import cn.zeroeden.domain.attendance.entity.AttendanceConfig;
import cn.zeroeden.domain.attendance.vo.AtteUploadVo;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.poi.ExcelImportUtil;
import cn.zeroeden.utils.DateUtil;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Service
public class ExcelImportServiceImpl implements ExcelImportService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private IdWorker idWorker;

    @Value("${atte.workingDays}")
    private String workingDays;

	@Value("${atte.holidays}")
	private String holidays;

    @Override
    public void importAttendanceByExcel(MultipartFile file, String companyId) throws Exception{
        // 将导入的Excel文件解析为vo的list集合
        List<AtteUploadVo> vo = new ExcelImportUtil<AtteUploadVo>(AtteUploadVo.class).readExcel(file.getInputStream(), 1, 0);
        // 循环list集合
        for (AtteUploadVo atteUploadVo : vo) {
            //   2.1 根据其手机号码查询用户信息
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getMobile, atteUploadVo.getMobile());
            User user = userDao.selectOne(queryWrapper);
            //   2.2 构造考勤对象
            Attendance attendance = new Attendance(atteUploadVo, user);
            attendance.setDay(atteUploadVo.getAtteDate());
            //   2.3 判断是否休假
            /**
             * 将国家的假日记录到数据/文件中
             */
            if(holidays.contains(atteUploadVo.getAtteDate())){
                // 国家假日
                attendance.setAdtStatu(23); // 休息
            } else if (DateUtil.isWeekend(atteUploadVo.getAtteDate()) || !workingDays.contains(atteUploadVo.getAtteDate())) {
                // 周末/非工作日
                attendance.setAdtStatu(23); // 休息
            }else{
                // 正常上班日
                //   2.4 判断迟到、早退的状态
                // 根据公司id和部门id查询此员工所在部门的考勤时间配置
                LambdaQueryWrapper<AttendanceConfig> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(AttendanceConfig::getCompanyId, companyId);
                wrapper.eq(AttendanceConfig::getDepartmentId, user.getDepartmentId());
                AttendanceConfig ac = attendanceConfigDao.selectOne(wrapper);
                // 比较是否迟到
                if(!DateUtil.comparingDate(ac.getAfternoonStartTime(), atteUploadVo.getInTime())){
                    // 迟到
                    attendance.setAdtStatu(3);
                }else if(DateUtil.comparingDate(ac.getAfternoonEndTime(), atteUploadVo.getOutTime())){
                    // 早退
                    attendance.setAdtStatu(4);
                }else{
                    // 正常
                    attendance.setAdtStatu(1);
                }
            }

            //   2.5 查询用户是否已经有考勤记录，如果不存在，保存数据库--防止偷鸡
            LambdaQueryWrapper<Attendance> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Attendance::getUserId, user.getId());
            queryWrapper1.eq(Attendance::getDay,atteUploadVo.getAtteDate());
            Attendance attendance1 = attendanceDao.selectOne(queryWrapper1);
            if(attendance1 == null){
                // 不存在考勤记录，插入
                attendance.setCompanyId(idWorker.nextId() + "");
                attendanceDao.insert(attendance);
            }
        }

    }
}
