package cn.zeroeden.audit.client;

import cn.zeroeden.domain.company.Department;
import cn.zeroeden.domain.employee.UserCompanyJobs;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.entity.Result;
import cn.zeroeden.system.client.DepartmentFeignClient;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeignClientService {

	@Autowired
	private SystemFeignClient systemFeignClient;
	@Autowired
	private DepartmentFeignClient departmentFeignClient;
	@Autowired
	private EmployeeFeignClient employeeFeignClient;


	// 查询岗位信息
	public UserCompanyJobs getUserCompanyJobsInfoByDepartmentId(String user_id) {
		Result result = employeeFeignClient.findJobsInfo(user_id);
		if (!result.isSuccess()) throw new RuntimeException("用户ID不存在，请联系管理员：" + user_id);
		return JSON.parseObject(JSON.toJSONString(result.getData()), UserCompanyJobs.class);
	}

	// 查询部门信息
	public Department getDepartmentInfoByDepartmentId(String departmentId) {
		if (departmentId == null) return null;
		Result result = departmentFeignClient.findById(departmentId);
		if (!result.isSuccess()) throw new RuntimeException("用户ID不存在，请联系管理员：" + departmentId);
		return JSON.parseObject(JSON.toJSONString(result.getData()), Department.class);
	}

	// 查询用户信息
	public User getUserInfoByUserId(String user_id) {
		if (user_id == null) throw new RuntimeException("获取当前用户为空！");
		Result result = systemFeignClient.findById(user_id);
		if (!result.isSuccess()) throw new RuntimeException("用户ID不存在，请联系管理员：" + user_id);
		return JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
	}


}
