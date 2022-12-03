package cn.zeroeden.attendance.service;

import cn.zeroeden.domain.attendance.entity.AttendanceConfig;
import cn.zeroeden.domain.attendance.entity.DeductionDict;
import cn.zeroeden.domain.attendance.entity.LeaveConfig;
import cn.zeroeden.domain.attendance.vo.ExtDutyVO;

import java.util.List;
import java.util.Map;

public interface ConfigurationService{


    /**
     * 根据公司id和部门id获取其公司的此部门考勤配置
     * @param companyId 公司id
     * @param departmentId 部门id
     * @return 此公司的此部门的考勤配置
     */
    AttendanceConfig getAttendanceConfig(String companyId, String departmentId);

    /**
     * 保存/更新某个公司的某部门的考勤配置
     * @param attendanceConfig 考勤配置
     */
    void saveOrUpdateAtteConfig(AttendanceConfig attendanceConfig);

    /**
     * 保存/更新请假配置
     * @param leaveConfig 请假配置
     * @param userId 用户id
     */
    void leaveConfigSaveOrUpdate(LeaveConfig leaveConfig, String userId);

    /**
     * 保存/更新扣款
     * @param deductionDict 扣款配置
     * @param userId 用户id
     */
    void deductionDictSaveOrUpdate(DeductionDict deductionDict, String userId);

    /**
     * 保存/更新加班
     * @param atteExtDutyVO 加班配置
     * @param userId 用户id
     */
    void extDutySaveOrUpdate(ExtDutyVO atteExtDutyVO, String userId);

    /**
     * 请假设置信息配置项查询
     * @param companyId 公司id
     * @param departmentId 部门id
     * @return 请假设置信息配置项
     */
    List<LeaveConfig> getLeaveCfg(String companyId, String departmentId);

    /**
     * 扣款信息配置项查询
     * @param companyId 公司id
     * @param departmentId 部门id
     * @return 扣款信息配置项
     */
    List<DeductionDict> getDedCfgList(String companyId, String departmentId);

    /**
     * 加班信息配置项查询
     * @param companyId 公司id
     * @param departmentId 部门id
     * @return 加班信息配置项
     */
    Map getExtWorkCfg(String companyId, String departmentId);
}
