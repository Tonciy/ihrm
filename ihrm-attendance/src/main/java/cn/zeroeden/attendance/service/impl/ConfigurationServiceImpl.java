package cn.zeroeden.attendance.service.impl;

import cn.zeroeden.attendance.dao.*;
import cn.zeroeden.attendance.service.ConfigurationService;
import cn.zeroeden.domain.attendance.entity.AttendanceConfig;
import cn.zeroeden.domain.attendance.entity.DeductionDict;
import cn.zeroeden.domain.attendance.entity.LeaveConfig;
import cn.zeroeden.domain.attendance.vo.ExtDutyVO;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private LeaveConfigDao leaveConfigDao;

    @Autowired
    private DeductionDictDao deductionDictDao;

    @Autowired
    private ExtraDutyConfigDao extraDutyConfigDao;

    @Autowired
    private ExtraDutyRuleDao extraDutyRuleDao;

    @Autowired
    private DayOffConfigDao dayOffConfigDao;

    @Autowired
    private IdWorker idWorker;


    @Override
    public AttendanceConfig getAttendanceConfig(String companyId, String departmentId) {
        LambdaQueryWrapper<AttendanceConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendanceConfig::getCompanyId, companyId);
        queryWrapper.eq(AttendanceConfig::getDepartmentId, departmentId);
        AttendanceConfig ac = attendanceConfigDao.selectOne(queryWrapper);
        return ac;
    }

    @Override
    public void saveOrUpdateAtteConfig(AttendanceConfig attendanceConfig) {
        // 先根据公司id和部门id查询此公司此部门的考勤配置
        LambdaQueryWrapper<AttendanceConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendanceConfig::getCompanyId, attendanceConfig.getCompanyId());
        queryWrapper.eq(AttendanceConfig::getDepartmentId, attendanceConfig.getDepartmentId());
        AttendanceConfig ac = attendanceConfigDao.selectOne(queryWrapper);
        if(ac == null){
            // 无考勤配置-新增
            attendanceConfig.setId(idWorker.nextId() + "");
            attendanceConfigDao.insert(attendanceConfig);
        }else{
            // 有考勤配置-更新
            attendanceConfig.setId(ac.getId());
            attendanceConfigDao.updateById(attendanceConfig);
        }
    }

    @Override
    public void leaveConfigSaveOrUpdate(LeaveConfig leaveConfig, String userId) {
            // 暂时不确定根据啥来查
    }

    @Override
    public void deductionDictSaveOrUpdate(DeductionDict deductionDict, String userId) {
        LambdaQueryWrapper<DeductionDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeductionDict::getCompanyId, deductionDict.getCompanyId());
        queryWrapper.eq(DeductionDict::getDepartmentId, deductionDict.getDepartmentId());
        DeductionDict target = deductionDictDao.selectOne(queryWrapper);
        if(target == null){
            // 新增
            deductionDict.setId(idWorker.nextId() + "");
            deductionDictDao.insert(deductionDict);
        }else{
            // 更新
            deductionDict.setCompanyId(target.getId());
            deductionDictDao.updateById(deductionDict);
        }
    }

    @Override
    public void extDutySaveOrUpdate(ExtDutyVO atteExtDutyVO, String userId) {
        // 暂时没看懂
    }

    @Override
    public List<LeaveConfig> getLeaveCfg(String companyId, String departmentId) {
        LambdaQueryWrapper<LeaveConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeaveConfig::getCompanyId, companyId);
        queryWrapper.eq(LeaveConfig::getDepartmentId, departmentId);
        List<LeaveConfig> list = leaveConfigDao.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<DeductionDict> getDedCfgList(String companyId, String departmentId) {
        LambdaQueryWrapper<DeductionDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeductionDict::getCompanyId, companyId);
        queryWrapper.eq(DeductionDict::getDepartmentId, departmentId);
        List<DeductionDict> list = deductionDictDao.selectList(queryWrapper);
        return list;
    }

    @Override
    public Map getExtWorkCfg(String companyId, String departmentId) {
        return null;
    }
}
