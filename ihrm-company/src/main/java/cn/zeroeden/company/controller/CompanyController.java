package cn.zeroeden.company.controller;

import cn.zeroeden.company.service.CompanyService;
import cn.zeroeden.domain.company.Company;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zero
 * @Description 描述此类
 */
@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 添加企业
     */
    @PostMapping
    public Result add(@RequestBody Company company) throws Exception {
        companyService.add(company);
        return Result.SUCCESS();
    }
    /**
     * 根据id更新企业信息
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable(name = "id") String id,
                         @RequestBody Company company) throws Exception {
        company.setId(id);
        companyService.updateById(company);
        return Result.SUCCESS();
    }
    /**
     * 根据id删除企业信息
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        companyService.removeById(id);
        return Result.SUCCESS();
    }
    /**
     * 根据ID获取公司信息
     */
   @GetMapping("/{id}")
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Company company = companyService.getById(id);
        return new Result(ResultCode.SUCCESS, company);
    }
    /**
     * 获取企业列表
     */
   @GetMapping
    public Result findAll() throws Exception {
        List<Company> companyList = companyService.list();
        return new Result(ResultCode.SUCCESS,companyList);
    }
}
