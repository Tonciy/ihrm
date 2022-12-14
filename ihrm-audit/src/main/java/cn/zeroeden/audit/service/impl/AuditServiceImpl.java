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
        // ????????????
        queryWrapper.eq(!StringUtils.isEmpty(procInstance.getProcessKey()), ProcInstance::getProcessKey, procInstance.getProcessKey());
        // ???????????????????????????
        queryWrapper.eq(!StringUtils.isEmpty(procInstance.getProcCurrNodeUserId()), ProcInstance::getProcCurrNodeUserId, procInstance.getProcCurrNodeUserId());
        // ?????????????????????????????????????????????
        queryWrapper.like(!StringUtils.isEmpty(procInstance.getProcessState()), ProcInstance::getProcessState, procInstance.getProcessState());
        // ?????????
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
        // 1. ??????????????????
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
        // 2. ??????????????????
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(map.get("processKey").toString())
                .processDefinitionTenantId(companyId)
                .latestVersion()
                .singleResult();
        // 3. ????????????
        HashMap<String, Object> vars = new HashMap<>();
        if("process_leave".equals(map.get("processKey"))){
            // ??????
            vars.put("days", map.get("duration"));
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definition.getId(), instance.getProcessId(), vars);// ???????????????id??? ????????????id??? ???????????????
        // 4. ????????????????????????????????????????????????????????????????????????
        // 4.1 ??????????????????????????????
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        // 4.2 ??????
        taskService.complete(task.getId());

        // 5. ?????????????????????????????????????????????????????????????????????
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
        pti.setHandleType("2"); // 2 ???????????????
        pti.setHandleUserId(map.get("userId").toString());
        pti.setHandleUserName(user.getUsername());
        pti.setTaskKey(task.getTaskDefinitionKey());
        pti.setTaskName(task.getName());
        pti.setHandleOpinion("????????????");
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
        // ???????????????mybaits???????????????sql
//        Query query = entityManager.createNativeQuery(sql);
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(EmployeeNameOnlyDto.class));
//        users.addAll(query.getResultList());
    }
		return users;
}

    @Override
    public void commit(ProcTaskInstance taskInstance, String companyId) {
        // 1. ????????????????????????
        String processId = taskInstance.getProcessId();
        LambdaQueryWrapper<ProcInstance> queryWrapper = new LambdaQueryWrapper<ProcInstance>().eq(ProcInstance::getProcessId, processId);
        ProcInstance instance = procInstanceDao.selectOne(queryWrapper);
        // 2. ????????????????????????
        instance.setProcessState(taskInstance.getHandleType());
        // 3. ?????????????????????????????????????????????????????????
        // ?????????activiti??????????????????????????????????????????id??????activiti?????????????????????
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(processId).singleResult();
        User user = feignClientService.getUserInfoByUserId(taskInstance.getHandleUserId());
        if("2".equals(taskInstance.getHandleType())){
            // ????????????
            // ????????????????????????????????????????????????
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            taskService.complete(task.getId());
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????
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
                // ???????????????????????????????????????
                instance.setProcessState("2");
            }
        }else{
            // ?????????????????????????????????activiti?????????
            runtimeService.deleteProcessInstance(processInstance.getId(),taskInstance.getHandleOpinion());
        }
        // 4. ???????????????????????????????????????????????????
        procInstanceDao.insert(instance);
        taskInstance.setTaskId(idWorker.nextId() + "");
        taskInstance.setHandleUserName(user.getUsername());
        taskInstance.setHandleTime(new Date());
        procTaskInstanceDao.insert(taskInstance);

    }
}






















