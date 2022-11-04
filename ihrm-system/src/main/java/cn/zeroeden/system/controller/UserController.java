package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.entity.UserResult;
import cn.zeroeden.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2022/11/2
 * @description:
 */
@RestController
@RequestMapping("/sys")
@CrossOrigin
public class UserController extends BaseController {
    @Autowired
    private UserService userService;


    @PostMapping("/user")
    public Result save(@RequestBody User user){
        // 1. 设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.add(user);
        return Result.SUCCESS();
    }

    /**
     * 根据用户
     * @return
     */
    @GetMapping("/user")
    public Result findAll(int page, int size, @RequestParam Map map){
        //TODO 假设当前用户的企业id
        map.put("companyId", companyId);
        IPage res = userService.findAll(map, page, size);
        PageResult<User> r = new PageResult<>(res.getTotal(),res.getRecords());
        return new Result(ResultCode.SUCCESS, r);
    }

    /**
     * 根据id查询user
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public Result findById(@PathVariable String id){
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    /**
     * 根据id修改user
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/user/{id}")
    public Result updateById(@PathVariable String id, @RequestBody User user){
        // 设置部门的id
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public Result deleteById(@PathVariable String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 为用户分配角色
     * @param map  装载了用户id和待分配的角色ids
     * @return
     */
    @PutMapping("/user/assignRoles")
    public Result assignRoles(@RequestBody Map<String, Object> map){
        String id  = (String)map.get("id");
        List<String> roleIds = (List<String>)map.get("ids");
        userService.assignRoles(id, roleIds);
        return Result.SUCCESS();
    }
}
