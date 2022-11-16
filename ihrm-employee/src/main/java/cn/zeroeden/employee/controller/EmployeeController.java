package cn.zeroeden.employee.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.employee.*;
import cn.zeroeden.domain.employee.response.EmployeeReportResult;
import cn.zeroeden.employee.service.*;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.poi.ExcelExportUtil;
import cn.zeroeden.utils.BeanMapUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;


    /**
     * 输出员工个人信息PDF报表
     * @throws Exception
     */
    @GetMapping("/{id}/pdf")
    public void pdf(@PathVariable("id") String id) throws Exception{
        // 1. 引入jasper文件
        Resource resource = new ClassPathResource("templates/profile.jasper");
        FileInputStream fis = new FileInputStream(resource.getFile());
        // 2. 构造数据
        UserCompanyPersonal personal = userCompanyPersonalService.findById(id);
        UserCompanyJobs jobs = userCompanyJobsService.findById(id);
        String staffPhoto = "http://rla1xej99.hn-bkt.clouddn.com/" + id;
        // 3. 填充PDF模板数据
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("staffPhoto", staffPhoto);
        Map<String, Object> map1 = BeanMapUtils.beanToMap(personal);
        Map<String, Object> map2 = BeanMapUtils.beanToMap(jobs);
        parms.putAll(map1);
        parms.putAll(map2);
        ServletOutputStream os = response.getOutputStream();
        try {
            JasperPrint print = JasperFillManager.fillReport(fis, parms, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, os);
        }catch (JRException e){
            e.printStackTrace();
        }finally {
            os.flush();
        }
    }



    /**
     * 当月认识报表导出
     *
     * @param month 2022-02  这样的形式
     * @throws Exception
     */
    @GetMapping("/export/{month}")
    public void export(@PathVariable(name = "month") String month) throws Exception {
        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId, month);
        // 使用模板构建
        Resource resource = new ClassPathResource("excelTemplate\\hr-demo.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());
        // 通过工具类是显示Excel报表导出
        new ExcelExportUtil<EmployeeReportResult>(EmployeeReportResult.class, 2, 3)
                .export(response, fis, list, month + "人事报表");

        // 下方是使用模板构建
//        XSSFWorkbook workbook = new XSSFWorkbook(fis);
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        XSSFRow row = sheet.getRow(2);
//        // 抽取公共样式
//        CellStyle[] common = new CellStyle[row.getLastCellNum()];
//        for (int i = 0; i < row.getLastCellNum(); i++) {
//            XSSFCell cell = row.getCell(i);
//            common[i] = cell.getCellStyle();
//        }
//        int rowIndex = 2;
//        Cell cell = null;
//        for (EmployeeReportResult report : list) {
//            XSSFRow dataRow = sheet.createRow(rowIndex++);
//            cell = dataRow.createCell(0);
//            cell.setCellValue(report.getUserId());
//            cell.setCellStyle(common[0]);
////姓名
//            cell = dataRow.createCell(1);
//            cell.setCellValue(report.getUsername());
//            cell.setCellStyle(common[1]);
////手机
//            cell = dataRow.createCell(2);
//            cell.setCellValue(report.getMobile());
//            cell.setCellStyle(common[2]);
////最高学历
//            cell = dataRow.createCell(3);
//            cell.setCellValue(report.getTheHighestDegreeOfEducation());
//            cell.setCellStyle(common[3]);
////国家地区
//            cell = dataRow.createCell(4);
//            cell.setCellValue(report.getNationalArea());
//            cell.setCellStyle(common[4]);
////护照号
//            cell = dataRow.createCell(5);
//            cell.setCellValue(report.getPassportNo());
//            cell.setCellStyle(common[5]);
////籍贯
//            cell = dataRow.createCell(6);
//            cell.setCellValue(report.getNativePlace());
//            cell.setCellStyle(common[6]);
////生日
//            cell = dataRow.createCell(7);
//            cell.setCellValue(report.getBirthday());
//            cell.setCellStyle(common[7]);
////属相
//            cell = dataRow.createCell(8);
//            cell.setCellValue(report.getZodiac());
//            cell.setCellStyle(common[8]);
////入职时间
//            cell = dataRow.createCell(9);
//            cell.setCellValue(report.getTimeOfEntry());
//            cell.setCellStyle(common[9]);
////离职类型
//            cell = dataRow.createCell(10);
//            cell.setCellValue(report.getTypeOfTurnover());
//            cell.setCellStyle(common[10]);
////离职原因
//            cell = dataRow.createCell(11);
//            cell.setCellValue(report.getReasonsForLeaving());
//            cell.setCellStyle(common[11]);
////离职时间
//            cell = dataRow.createCell(12);
//            cell.setCellValue(report.getResignationTime());
//            cell.setCellStyle(common[12]);
//
//        }
//        // 设置标题
//        XSSFCell cell1 = sheet.getRow(0).getCell(0);
//        cell1.setCellValue(month + "人事报表");
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        workbook.write(os);
//        new DownloadUtils().download(os, response, month + "人事报表.xlsx");
        // 下方是传统构建
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet();
//        String[] titles = {"编号", "姓名", "手机", "最高学历", "国家地区", "护照号", "籍贯",
//                "生日", "属相", "入职时间", "离职类型", "离职原因", "离职时间"};
//        XSSFRow row = sheet.createRow(0);
//        int titleIndex = 0;
//        for (String title : titles) {
//            XSSFCell cell = row.createCell(titleIndex++);
//            cell.setCellValue(title);
//        }
//        int rowIndex = 1;
//        Cell cell = null;
//        for (EmployeeReportResult report : list) {
//            XSSFRow dataRow = sheet.createRow(rowIndex++);
//            cell = dataRow.createCell(0);
//            cell.setCellValue(report.getUserId());
////姓名
//            cell = dataRow.createCell(1);
//            cell.setCellValue(report.getUsername());
////手机
//            cell = dataRow.createCell(2);
//            cell.setCellValue(report.getMobile());
////最高学历
//            cell = dataRow.createCell(3);
//            cell.setCellValue(report.getTheHighestDegreeOfEducation());
////国家地区
//            cell = dataRow.createCell(4);
//            cell.setCellValue(report.getNationalArea());
////护照号
//            cell = dataRow.createCell(5);
//            cell.setCellValue(report.getPassportNo());
////籍贯
//            cell = dataRow.createCell(6);
//            cell.setCellValue(report.getNativePlace());
////生日
//            cell = dataRow.createCell(7);
//            cell.setCellValue(report.getBirthday());
////属相
//            cell = dataRow.createCell(8);
//            cell.setCellValue(report.getZodiac());
////入职时间
//            cell = dataRow.createCell(9);
//            cell.setCellValue(report.getTimeOfEntry());
////离职类型
//            cell = dataRow.createCell(10);
//            cell.setCellValue(report.getTypeOfTurnover());
////离职原因
//            cell = dataRow.createCell(11);
//            cell.setCellValue(report.getReasonsForLeaving());
////离职时间
//            cell = dataRow.createCell(12);
//            cell.setCellValue(report.getResignationTime());
//
//        }
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        workbook.write(os);
//        new DownloadUtils().download(os, response, month + "人事报表.xlsx");

    }

    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.mySave(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if (info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.mySave(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(super.userId);
        if (info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.mySave(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if (resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, resignation);
    }

    /**
     * 导入员工
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.mySave(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if (jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.mySave(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if (positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year", year);
        map.put("companyId", companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotal(), searchPage.getRecords());
        return new Result(ResultCode.SUCCESS, pr);
    }
}
