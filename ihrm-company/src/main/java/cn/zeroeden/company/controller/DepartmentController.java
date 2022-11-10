package cn.zeroeden.company.controller;

import cn.zeroeden.company.service.CompanyService;
import cn.zeroeden.company.service.DepartmentService;
import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.company.Company;
import cn.zeroeden.domain.company.Department;
import cn.zeroeden.domain.company.response.DeptListResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/department/search")
    public Department finDByCodeAndCompanyId(@RequestParam("code") String code,
                                             @RequestParam("companyId")String companyId){
        return departmentService.findByCode(code, companyId);
    }

    @PostMapping("/department")
    public Result save(@RequestBody Department department){
        // 1. 设置保存的企业id
        department.setCompanyId(companyId);
        departmentService.add(department);
        return Result.SUCCESS();
    }

    /**
     * 根据公司id查询其所有部门信息
     * @return
     */
    @GetMapping("/department")
    public Result findAllByDepartmentId(){
        // 1. 指定企业 id
        Company company = companyService.getById(companyId);
        List<Department> list = departmentService.findAllByCompanyId(companyId);
        DeptListResult res = new DeptListResult(company, list);
        return new Result(ResultCode.SUCCESS, res);
    }

    /**
     * 根据id查询department
     * @param id
     * @return
     */
    @GetMapping("/department/{id}")
    public Result findById(@PathVariable String id){
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS, department);
    }

    /**
     * 根据id修改department
     * @param id
     * @param department
     * @return
     */
    @PutMapping("/department/{id}")
    public Result updateById(@PathVariable String id, @RequestBody Department department){
        // 设置部门的id
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除部门
     * @param id
     * @return
     */
    @DeleteMapping("/department/{id}")
    public Result deleteById(@PathVariable String id){
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
