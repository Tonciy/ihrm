package cn.zeroeden.audit.service;

import cn.zeroeden.audit.entity.ProcInstance;
import cn.zeroeden.audit.entity.ProcTaskInstance;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

public interface AuditService {
    /**
     * 分页查询所有实例
     * @param procInstance 查询参数
     * @param page 当前页
     * @param size 一页展示数量
     * @return 数据
     */
    IPage<ProcInstance> getInstanceList(ProcInstance procInstance, int page, int size);

    /**
     * 根据id查询详情数据
     * @param id id
     * @return 数据
     */
    ProcInstance findInstanceDetail(String id);

    /**
     * 流程神奇
     * @param map 参数
     * @param companyId 公司id
     */
    void startProcess(Map map, String companyId);

    /**
     * 提交审核
     * @param taskInstance 参数封装
     * @param companyId 公司id
     */
    void commit(ProcTaskInstance taskInstance, String companyId);
}
