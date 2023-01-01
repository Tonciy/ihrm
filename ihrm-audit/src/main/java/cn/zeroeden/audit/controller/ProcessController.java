package cn.zeroeden.audit.controller;

import cn.zeroeden.audit.entity.ProcInstance;
import cn.zeroeden.audit.entity.ProcTaskInstance;
import cn.zeroeden.audit.service.AuditService;
import cn.zeroeden.audit.service.ProcessService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AuditService auditService;

    /**
     * 部署新的流程
     *
     * @param file 前端绘制好的流程模型图(bpmn)
     * @return 无
     */
    @PostMapping("/deploy")
    public Result deployProcess(@RequestParam("file") MultipartFile file) throws IOException {
        processService.deployProcess(file, companyId);
        return Result.SUCCESS();
    }

    /**
     * 获取所有的流程定义
     *
     * @return 所有的流程定义数据
     * @throws Exception 统一
     */
    @GetMapping("/definition")
    public Result getProcessDefinitionList() throws Exception {
        List list = processService.getProcessDefinitionList(companyId);
        return Result.SUCCESS(list);
    }

    /**
     * 设置流程的挂起/激活状态
     *
     * @param processKey 流程key
     * @return 无
     * @throws Exception 统一
     */
    @GetMapping("/suspend/{processKey}")
    public Result suspendProcess(@PathVariable String processKey) throws Exception {
        processService.suspendProcess(processKey, companyId);
        return Result.SUCCESS();
    }

    /**
     * 分页查询申请列表
     * @param page 当前页
     * @param size 一页展示数量
     * @param instance 查询可能带有的参数（审批类型，审批状态，当前结点的待处理人）
     * @return 数据
     */
    @PutMapping("/instance/{page}/{size}")
    public Result instanceList(@PathVariable int page,
                               @PathVariable int size,
                               @RequestBody ProcInstance instance) {
        IPage<ProcInstance> pages = auditService.getInstanceList(instance, page, size);
        PageResult<ProcInstance> result = new PageResult<>(pages.getTotal(), pages.getRecords());
        return Result.SUCCESS(result);
    }

    /**
     * 根据id查询详情数据
     * @param id id
     * @return 数据
     * @throws Exception 统一
     */
    @GetMapping("/instance/{id}")
    public Result instanceDetail(@PathVariable String id) throws Exception{
        ProcInstance instance = auditService.findInstanceDetail(id);
        return Result.SUCCESS(instance);
    }

    /**
     * 流程申请
     * @param map 参数封装
     * @return 无
     * @throws Exception 统一
     */
    @PostMapping("/startProcess")
    public Result startProcess(@RequestBody Map map) throws Exception{
        auditService.startProcess(map, companyId);
        return Result.SUCCESS();
    }

    /**
     * 提交审核
     * @param taskInstance 参数封装
     * @return 无
     * @throws IOException 统一
     */
    @PutMapping("/instance/commit")
    public Result commit(@RequestBody ProcTaskInstance taskInstance) throws IOException{
        auditService.commit(taskInstance, companyId);
        return Result.SUCCESS();
    }

}
