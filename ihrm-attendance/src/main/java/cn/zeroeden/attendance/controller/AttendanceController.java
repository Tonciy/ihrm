package cn.zeroeden.attendance.controller;

import cn.zeroeden.attendance.service.ArchiveService;
import cn.zeroeden.attendance.service.AtteService;
import cn.zeroeden.attendance.service.ExcelImportService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthly;
import cn.zeroeden.domain.attendance.entity.ArchiveMonthlyInfo;
import cn.zeroeden.domain.attendance.entity.Attendance;
import cn.zeroeden.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2022/12/3
 * @description: 考勤方面的功能接口
 */

@RestController
@RequestMapping("/attendances")
public class AttendanceController extends BaseController {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private AtteService atteService;

    @Autowired
    private ArchiveService archiveService;
    /**
     * 通过Excel将考勤机的数据导入到此系统中
     * @param file 装载考勤数据的Excel
     * @return 无
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam(name = "file") MultipartFile file) throws Exception {
        excelImportService.importAttendanceByExcel(file, companyId);
        return Result.SUCCESS();
    }

    /**
     * 获取某公司的考勤数据列表-分页（）
     *       data:用户的考勤数据
     *       monthOfReport: 处理的日期
     *       toeTaskCount: 带处理的考勤数量
     * @param page 当前页
     * @param pageSize 一页展示数量
     * @return 考勤数据列表
     * @throws Exception 统一捕获
     */
    @GetMapping
    public Result importExcel(int page, int pageSize) throws Exception{
        Map map = atteService.getAtteData(companyId, page, pageSize);
        return Result.SUCCESS(map);
    }

    /**
     * 编辑用户的考勤记录
     * @param attendance 装载用户的某天的额考勤记录
     * @return 统一返回
     * @throws Exception 无
     */
    @PutMapping("/{id}")
    public Result editAtte(@RequestBody Attendance attendance) throws Exception{
        atteService.editAtte(attendance);
        return Result.SUCCESS();
    }

    /**
     * 获取某年月的考勤报表归档数据
     * @param atteDate 年月
     * @return 具体数据
     * @throws Exception 无
     */
    @GetMapping("/reports")
    public Result reports(String atteDate) throws Exception{
        List<ArchiveMonthlyInfo> list = atteService.getReports(atteDate, companyId);
        return Result.SUCCESS(list);
    }

    /**
     * 保存考勤报表归档数据
     * @param archiveDate 年月
     * @return 无
     * @throws Exception 无
     */
    @GetMapping("/archive/item")
    public Result saveArchive(String archiveDate) throws Exception{
        archiveService.saveArchive(archiveDate, companyId);
        return Result.SUCCESS();
    }

    /**
     * 新建考勤报表
     * @param yearMonth 新建考勤报表的年份
     * @return 无
     * @throws Exception 统一
     */
    @GetMapping("/newReports")
    public Result newReports(String  yearMonth) throws Exception{
        atteService.newReports(yearMonth, companyId);
        return Result.SUCCESS();
    }

    /**
     * 查询归档历史列表
     * @param year 年份
     * @return 无
     * @throws Exception 统一
     */
    @GetMapping("/reports/year")
    public Result findReportsByYear(String year) throws Exception{
        List<ArchiveMonthly> list = archiveService.findReportsByYear(year, companyId);
        return Result.SUCCESS(list);
    }

    /**
     * 查询某个月的考勤归档详情
     * @param id 主表id
     * @return 数据
     * @throws Exception 统一
     */
    @GetMapping("/reports/{id}")
    public Result findInfosById(@PathVariable String id) throws Exception{
        List<ArchiveMonthlyInfo> list = archiveService.findMonthlyInfoByAmid(id);
        return Result.SUCCESS(list);
    }

    /**
     * 根据用户id和年月查询已归档的用户考勤数据
     * @param userId 用户id
     * @param yearMonth 年月
     * @return 用户的已经归档的考勤数据
     * @throws Exception 统一
     */
    @GetMapping("/archive/{userId}/{yearMonth}")
    public Result historyData(@PathVariable String userId, @PathVariable String yearMonth) throws Exception{
        ArchiveMonthlyInfo info = archiveService.findUserArchiveDetail(userId, yearMonth);
        return Result.SUCCESS(info);
    }
}
