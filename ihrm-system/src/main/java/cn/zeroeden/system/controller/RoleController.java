package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.system.Role;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.entity.RoleResult;
import cn.zeroeden.system.service.RoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        roleService.add(role);
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
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS, roleResult);
    }

    /**
     * 分页查询角色
     */
    @RequestMapping("/role")
    public Result findByPage(int page, int pagesize, Role role) throws Exception {
        String companyId = "1";
        IPage<Role> searchPage = roleService.findSearch(companyId, page, pagesize);
        List<Role> roles = searchPage.getRecords();
        for (int i = 0; i < roles.size(); i++) {
            Role role1 = roles.get(i);
            role1 = roleService.findUsers(role1);
            role1 = roleService.findPermissions(role1);
            roles.set(i, role1);
        }
        PageResult<Role> pr = new
                PageResult(searchPage.getTotal(), roles);
        return new Result(ResultCode.SUCCESS, pr);
    }

    /**
     * 为角色赋予权限
     * @param map 装载了角色roleId 和 权限集合permissionIds
     * @return
     */
    @PutMapping("/role/assignPrem")
    public Result save(@RequestBody Map<String, Object> map){
        String roleId = (String)map.get("id");
        List<String> permissionIds = (List<String>)map.get("permIds");
        roleService.assignRoles(roleId, permissionIds);
        return Result.SUCCESS();
    }

    /**
     * 查询所有角色信息
     * @return 角色集合
     */
    @GetMapping("/role/list")
    public Result findAll(){
        List<Role> list = roleService.findAll();
        return Result.SUCCESS(list);
    }
}
