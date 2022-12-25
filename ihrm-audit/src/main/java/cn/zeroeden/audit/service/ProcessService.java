package cn.zeroeden.audit.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProcessService {
    /**
     * 部署新流程
     * @param file 前端绘制号的流程模型图（bpmn）
     * @param company 公司
     */
    void deployProcess(MultipartFile file, String company) throws IOException;
}
