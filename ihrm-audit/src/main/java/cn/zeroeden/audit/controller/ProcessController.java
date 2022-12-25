package cn.zeroeden.audit.controller;

import cn.zeroeden.audit.service.ProcessService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Zero
 * @time: 2022/12/25
 * @description: 流程控制
 */

@CrossOrigin
@RestController
@RequestMapping("/user/process")
public class ProcessController extends BaseController {
    @Autowired
    private ProcessService processService;


    /**
     * 部署新的流程
     * @param file 前端绘制好的流程模型图(bpmn)
     * @return 无
     */
    @PostMapping("/deploy")
    public Result deployProcess(@RequestParam("file")MultipartFile file) throws IOException {
        processService.deployProcess(file, companyId);
        return Result.SUCCESS();
    }
}
