package cn.zeroeden.salarys.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.salarys.SalaryArchive;
import cn.zeroeden.domain.salarys.SalaryArchiveDetail;
import cn.zeroeden.entity.Result;
import cn.zeroeden.salarys.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/salarys")
public class ArchiveController extends BaseController {


    @Autowired
    private ArchiveService archiveService;

    /**
     * 查看薪资报表数据
     * @param yearMonth 年月
     * @param opType 1：新制作报表，其他：查询已经归档的报表数据
     * @return 具体数据
     */
    @GetMapping("/reports/{yearMonth}")
    public Result historyDetail(@PathVariable String yearMonth, int opType){
        List<SalaryArchiveDetail> list = new ArrayList<>();
        // 判断opType 的值，如果 == 1，自己构造报表数据
        if(opType == 1){
            list = archiveService.getReports(yearMonth, companyId);
        }else{
            // 如果是其他，查询已经归档明细表的所有数据
            SalaryArchive sa = archiveService.findSalaryArchive(yearMonth, companyId);
            if(sa != null){
                list = archiveService.findSalaryArchiveDetail(sa.getId());
            }

        }
        return Result.SUCCESS(list);
    }
}
