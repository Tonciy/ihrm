package cn.zeroeden.audit.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProcessService {
    /**
     * 部署新流程
     * @param file 前端绘制号的流程模型图（bpmn）
     * @param company 公司
     */
    void deployProcess(MultipartFile file, String company) throws IOException;

    /**
     * 获取所有的流程定义
     * @param companyId 公司id
     * @return 所有的流程定义数据
     */
    List getProcessDefinitionList(String companyId);

    /**
     * 设置流程的挂起/激活状态
     * @param processKey 流程key
     * @param companyId 公司di
     */
    void suspendProcess(String processKey, String companyId);
}
