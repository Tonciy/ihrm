package cn.zeroeden.attendance.service;

import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import cn.zeroeden.domain.attendance.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface AtteService extends IService<Attendance> {


    /**
     * 根据公司id获取此公司的考勤数据列表
     * @param companyId 公司id
     * @param page 当前页
     * @param pageSize 一页展示数量
     * @return 考勤数据列表
     */
    Map getAtteData(String companyId, int page, int pageSize) throws Exception;

    /**
     * 编辑用户的考勤记录
     * @param attendance 装载了用户某天的考勤记录
     */
    void editAtte(Attendance attendance);

    /**
     * 获取某年月的考勤报表归档数据
     * @param atteDate 年月
     * @param companyId 公司id
     * @return 具体数据
     */
    List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId);

    /**
     * 新建新的月份的考勤报表
     * @param yearMonth 年月
     * @param companyId 公司id
     */
    void newReports(String yearMonth, String companyId);
}
