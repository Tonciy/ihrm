package cn.zeroeden.attendance.controller;

import cn.zeroeden.attendance.service.ExcelImportService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
