package cn.zeroeden.salarys.controller;


import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.salarys.UserSalary;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.salarys.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/salarys")
public class SalaryController extends BaseController {

	@Autowired
	private SalaryService salaryService;

	//查询用户薪资
	@RequestMapping(value = "/modify/{userId}", method = RequestMethod.GET)
	public Result modifyGet(@PathVariable(value = "userId") String userId) throws Exception {
		UserSalary userSalary = salaryService.findUserSalary(userId);
		return new Result(ResultCode.SUCCESS, userSalary);
	}

	//调薪
	@RequestMapping(value = "/modify/{userId}", method = RequestMethod.POST)
	public Result modify(@RequestBody UserSalary userSalary) throws Exception {
		salaryService.saveUserSalary(userSalary);
		return new Result(ResultCode.SUCCESS);
	}

	//定薪
	@RequestMapping(value = "/init/{userId}", method = RequestMethod.POST)
	public Result init(@RequestBody UserSalary userSalary) {
		userSalary.setFixedBasicSalary(userSalary.getCurrentBasicSalary());
		userSalary.setFixedPostWage(userSalary.getCurrentPostWage());
		salaryService.saveUserSalary(userSalary);
		return new Result(ResultCode.SUCCESS);
	}

	//查询列表
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Result list(@RequestBody Map map) {
		//1.获取请求参数,page,size
		Integer page = (Integer)map.get("page");
		Integer pageSize = (Integer)map.get("pageSize");
		//2.调用service查询
		PageResult pr = salaryService.findAll(page,pageSize,companyId);
		return new Result(ResultCode.SUCCESS, pr);
	}
}
