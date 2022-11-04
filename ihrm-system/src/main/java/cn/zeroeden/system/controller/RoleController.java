package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.system.Role;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.system.service.RoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description:
 */
@RestController
@RequestMapping("/sys")
@CrossOrigin
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    //添加角色
    @PostMapping("/role")
    public Result add(@RequestBody Role role) throws Exception {
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    //更新角色
    @PutMapping("/role/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role)
            throws Exception {
        roleService.update(role);
        return Result.SUCCESS();
    }

    //删除角色
    @DeleteMapping("/role/{id}")
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        roleService.delete(id);
        return Result.SUCCESS();
    }

    /**
     * 根据ID获取角色信息
     */
    @GetMapping("/role/{id}")
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS, role);
    }

    /**
     * 分页查询角色
     */
    @RequestMapping("/role")
    public Result findByPage(int page, int pagesize, Role role) throws Exception {
        String companyId = "1";
        IPage<Role> searchPage = roleService.findSearch(companyId, page, pagesize);
        PageResult<Role> pr = new
                PageResult(searchPage.getTotal(), searchPage.getRecords());
        return new Result(ResultCode.SUCCESS, pr);
    }
}
