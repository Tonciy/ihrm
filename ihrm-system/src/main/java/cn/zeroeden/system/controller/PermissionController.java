package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.system.Permission;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.system.service.PermissionService;
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
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;


    /**
     * 保存权限
     * @param tem
     * @return
     * @throws Exception
     */
    @PostMapping("/permission")
    public Result save(@RequestBody Map<String, Object> tem) throws Exception{
        permissionService.add(tem);
        return Result.SUCCESS();
    }

    /**
     * 根据查询所有权限
     * @return
     */
    @GetMapping("/permission")
    public Result findAll( Map<String, Object> map){
        List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }

    /**
     * 根据id查询permission
     * @param id
     * @return
     */
    @GetMapping("/permission/{id}")
    public Result findById(@PathVariable String id) throws Exception{
        Map<String, Object> map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,map);
    }

    /**
     * 根据id修改permission
     * @param id
     * @param map
     * @return
     */
    @PutMapping("/permission/{id}")
    public Result updateById(@PathVariable String id, Map<String, Object> map) throws Exception{
        map.put("id", id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/permission/{id}")
    public Result deleteById(@PathVariable String id) throws Exception{
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
