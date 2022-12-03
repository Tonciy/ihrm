package cn.zeroeden.attendance.controller;

import cn.zeroeden.attendance.service.ConfigurationService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.attendance.entity.AttendanceConfig;
import cn.zeroeden.domain.attendance.entity.DeductionDict;
import cn.zeroeden.domain.attendance.entity.LeaveConfig;
import cn.zeroeden.domain.attendance.vo.ExtDutyVO;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2022/12/3
 * @description: 有关考勤配置项设置的接口
 */
@RestController
@RequestMapping("/cfg")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    /**
     * 根据公司id和部门id获取某个公司某部门的考勤配置
     *
     * @param departmentId 部门id
     * @return 某个公司某部门的考勤配置
     */
    @PostMapping("/atte/item")
    public Result attendanceConfig(String departmentId) {
        AttendanceConfig ac = configurationService.getAttendanceConfig(companyId, departmentId);
        return Result.SUCCESS(ac);
    }

    /**
     * 保存/更新某个公司的某部门的考勤配置
     *
     * @param attendanceConfig 考勤配置实体
     * @return 无
     */
    @PutMapping("/atte")
    public Result saveOrUpdateAtteConfig(@RequestBody AttendanceConfig attendanceConfig) {
        attendanceConfig.setCompanyId(companyId);
        configurationService.saveOrUpdateAtteConfig(attendanceConfig);
        return Result.SUCCESS();
    }


    /**
     * 请假设置信息查询
     */
    @RequestMapping(value = "/leave/list", method = RequestMethod.POST)
    public Result leaveCfgItem(String departmentId) throws Exception {
        List<LeaveConfig> leaveConfigList =
                configurationService.getLeaveCfg(companyId,departmentId);
        return new Result(ResultCode.SUCCESS,leaveConfigList);
    }

    /**
     * 请假保存更新
     */
    @RequestMapping(value = "/leave", method = RequestMethod.PUT)
    public Result leaveSaveOrUpdate(@RequestBody List<LeaveConfig>
                                            leaveConfigList) {
        //公共
        for (LeaveConfig leaveConfig : leaveConfigList) {
            leaveConfig.setCompanyId(this.companyId);
            configurationService.leaveConfigSaveOrUpdate(leaveConfig, this.userId);
        }
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 扣款设置信息查询
     */
    @RequestMapping(value = "/ded/list", method = RequestMethod.POST)
    public Result dedCfgItem(String departmentId) throws Exception {
//公共
        List<DeductionDict> deductionDictList =
                configurationService.getDedCfgList(companyId,departmentId);
        return new Result(ResultCode.SUCCESS,deductionDictList);
    }


    /**
     * 扣款保存更新
     */
    @RequestMapping(value = "/deduction", method = RequestMethod.PUT)
    public Result deductionSaveOrUpdate(
            @RequestBody /*@Valid*/ List<DeductionDict> deductionDictList) {
        for (DeductionDict deductionDict : deductionDictList) {
//公共
            deductionDict.setCompanyId(this.companyId);
            configurationService.deductionDictSaveOrUpdate(deductionDict, this.userId);
        }
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 加班设置信息查询
     */
    @RequestMapping(value = "/extDuty/item", method = RequestMethod.POST)
    public Result extWorkCfgItem(String departmentId) throws CommonException {
        Map map = configurationService.getExtWorkCfg(companyId,departmentId);
        return new Result(ResultCode.SUCCESS,map);
    }

    /**
     * 加班保存更新
     */
    @RequestMapping(value = "/extDuty", method = RequestMethod.PUT)
    public Result extDutySaveOrUpdate(@RequestBody
                                      @Valid ExtDutyVO atteExtDutyVO) {
        atteExtDutyVO.setCompanyId(this.companyId);
        configurationService.extDutySaveOrUpdate(atteExtDutyVO, this.userId);
        return new Result(ResultCode.SUCCESS);
    }



}
