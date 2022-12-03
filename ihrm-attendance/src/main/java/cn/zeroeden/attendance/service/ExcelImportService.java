package cn.zeroeden.attendance.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {


    /**
     * 把考勤机的Excel数据导入到此系统中
     * @param file 装载考勤数据的Excel
     * @param companyId 公司id
     */
    void importAttendanceByExcel(MultipartFile file, String companyId) throws Exception;
}
