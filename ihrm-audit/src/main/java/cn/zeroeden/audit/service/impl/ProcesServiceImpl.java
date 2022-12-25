package cn.zeroeden.audit.service.impl;

import cn.zeroeden.audit.service.ProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Zero
 * @time: 2022/12/25
 * @description:
 */

@Service
public class ProcesServiceImpl implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void deployProcess(MultipartFile file, String companyId) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 进行流程部署
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addBytes(fileName, file.getBytes()).tenantId(companyId);
        deploymentBuilder.deploy();
    }
}
