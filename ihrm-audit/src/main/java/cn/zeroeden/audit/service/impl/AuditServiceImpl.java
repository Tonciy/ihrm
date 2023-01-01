package cn.zeroeden.audit.service.impl;

import cn.zeroeden.audit.client.FeignClientService;
import cn.zeroeden.audit.dao.ProcInstanceDao;
import cn.zeroeden.audit.dao.ProcTaskInstanceDao;
import cn.zeroeden.audit.dao.ProcUserGroupDao;
import cn.zeroeden.audit.entity.ProcInstance;
import cn.zeroeden.audit.entity.ProcTaskInstance;
import cn.zeroeden.audit.entity.ProcUserGroup;
import cn.zeroeden.audit.service.AuditService;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.utils.IdWorker;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: Zero
 * @time: 2023/1/1
 * @description:
 */

@Service
public class AuditServiceImpl implements AuditService {


    @Autowired
    private ProcInstanceDao procInstanceDao;

    @Autowired
    private FeignClientService feignClientService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcTaskInstanceDao procTaskInstanceDao;

    @Autowired
    private ProcUserGroupDao procUserGroupDao;


    @Override
    public IPage<ProcInstance> getInstanceList(ProcInstance procInstance, int page, int size) {
        IPage<ProcInstance> pages = new Page<>(page, size);
        LambdaQueryWrapper<ProcInstance> queryWrapper = new LambdaQueryWrapper<>();
        // 审批类型
        queryWrapper.eq(!StringUtils.isEmpty(procInstance.getProcessKey()), ProcInstance::getProcessKey, procInstance.getProcessKey());
        // 当前结点的待处理人
        queryWrapper.eq(!StringUtils.isEmpty(procInstance.getProcCurrNodeUserId()), ProcInstance::getProcCurrNodeUserId, procInstance.getProcCurrNodeUserId());
        // 审批状态（可能多个，以，分割）
        queryWrapper.like(!StringUtils.isEmpty(procInstance.getProcessState()), ProcInstance::getProcessState, procInstance.getProcessState());
        // 发起人
        queryWrapper.like(!StringUtils.isEmpty(procInstance.getProcCurrNodeUserId()), ProcInstance::getProcCurrNodeUserId, procInstance.getProcCurrNodeUserId());
        IPage<ProcInstance> procInstanceIPage = procInstanceDao.selectPage(pages, queryWrapper);
        return procInstanceIPage;
    }

    @Override
    public ProcInstance findInstanceDetail(String id) {
        LambdaQueryWrapper<ProcInstance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcInstance::getProcessId, id);
        ProcInstance instance = procInstanceDao.selectOne(queryWrapper);
        return instance;
    }

    @Override
    public void startProcess(Map map, String companyId) {
        // 1. 构造业务数据
        User user = feignClientService.getUserInfoByUserId(map.get("userId").toString());
        ProcInstance instance = new ProcInstance();
        BeanUtils.copyProperties(user, instance);
        instance.setUserId(map.get("userId").toString());
        instance.setProcessId(idWorker.nextId() + "");
        instance.setProcApplyTime(new Date());
        instance.setProcessKey(map.get("processKey").toString());
        instance.setProcessName(map.get("processName").toString());
        instance.setProcessState("1");
        String data = JSON.toJSONString(map);
        instance.setProcData(data);
        // 2. 查询流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(map.get("processKey").toString())
                .processDefinitionTenantId(companyId)
                .latestVersion()
                .singleResult();
        // 3. 开启流程
        HashMap<String, Object> vars = new HashMap<>();
        if("process_leave".equals(map.get("processKey"))){
            // 请假
            vars.put("days", map.get("duration"));
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definition.getId(), instance.getProcessId(), vars);// 流程定义的id， 业务数据id， 内置的参数
        // 4. 自动执行第一个任务结点（请假的第一节点不用审批）
        // 4.1 获取待执行的任务结点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        // 4.2 执行
        taskService.complete(task.getId());

        // 5. 获取下一个结点数据，填充业务数据汇总当前审批人
        Task next = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        if(next != null) {
            List<User> users = findCurrUsers(next, user);
            String usernames = "", userIdS = "";
            for (User user1 : users) {
                usernames += user1.getUsername() + " ";
                userIdS += user1.getId();
            }
            instance.setProcCurrNodeUserId(userIdS);
            instance.setProcCurrNodeUserId(usernames);
        }

        procInstanceDao.insert(instance);
        ProcTaskInstance pti = new ProcTaskInstance();
        pti.setTaskId(idWorker.nextId() + "");
        pti.setProcessId(instance.getProcessId());
        pti.setHandleTime(new Date());
        pti.setHandleType("2"); // 2 是请假类型
        pti.setHandleUserId(map.get("userId").toString());
        pti.setHandleUserName(user.getUsername());
        pti.setTaskKey(task.getTaskDefinitionKey());
        pti.setTaskName(task.getName());
        pti.setHandleOpinion("发情申请");
        procTaskInstanceDao.insert(pti);



    }
    private List<User> findCurrUsers(Task nextTask,User user) {
    List<IdentityLink> list = taskService.getIdentityLinksForTask(nextTask.getId());
    List<User> users = new ArrayList<>();
		for (IdentityLink identityLink : list) {
        String groupId = identityLink.getGroupId();
        ProcUserGroup userGroup = procUserGroupDao.selectById(groupId);
        String param = userGroup.getParam();
        String paramValue = null;
        if ("user_id".equals(param)) {
            paramValue = user.getId();
        }
        else if ("department_id".equals(param)) {
            paramValue = user.getDepartmentId();
        }
        else if ("company_id".equals(param)) {
            paramValue = user.getCompanyId();
        }
        String sql = userGroup.getIsql().replaceAll("\\$\\{" + param + "\\}", paramValue);
        // 应该转化为mybaits的主动调用sql
//        Query query = entityManager.createNativeQuery(sql);
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(EmployeeNameOnlyDto.class));
//        users.addAll(query.getResultList());
    }
		return users;
}

    @Override
    public void commit(ProcTaskInstance taskInstance, String companyId) {
        // 1. 查询业务流程对象
        String processId = taskInstance.getProcessId();
        LambdaQueryWrapper<ProcInstance> queryWrapper = new LambdaQueryWrapper<ProcInstance>().eq(ProcInstance::getProcessId, processId);
        ProcInstance instance = procInstanceDao.selectOne(queryWrapper);
        // 2. 设置业务流程状态
        instance.setProcessState(taskInstance.getHandleType());
        // 3. 根据不同的操作类型，完成不同的业务处理
        // 查询出activiti中的流程实例（根据自己的业务id查询activiti中的流程实例）
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(processId).singleResult();
        User user = feignClientService.getUserInfoByUserId(taskInstance.getHandleUserId());
        if("2".equals(taskInstance.getHandleType())){
            // 审核通过
            // 查询当前结点，并完成当前结点任务
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            taskService.complete(task.getId());
            // 查询出下一个任务结点，如果存在下一个流程，就进行对应设置，否则就结束
            Task next = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            if(next != null){
                List<User> users = findCurrUsers(next, user);
                String usernames = "", userIdS = "";
                for (User user1 : users) {
                    usernames += user1.getUsername() + " ";
                    userIdS += user1.getId();
                }
                instance.setProcCurrNodeUserId(userIdS);
                instance.setProcCurrNodeUserId(usernames);
                instance.setProcessState("1");
            }else{
                // 不存在下一个节点，任务结束
                instance.setProcessState("2");
            }
        }else{
            // 审核驳回或者撤销（删除activiti流程）
            runtimeService.deleteProcessInstance(processInstance.getId(),taskInstance.getHandleOpinion());
        }
        // 4. 更新业务流程对象，保存业务任务对象
        procInstanceDao.insert(instance);
        taskInstance.setTaskId(idWorker.nextId() + "");
        taskInstance.setHandleUserName(user.getUsername());
        taskInstance.setHandleTime(new Date());
        procTaskInstanceDao.insert(taskInstance);

    }
}






















