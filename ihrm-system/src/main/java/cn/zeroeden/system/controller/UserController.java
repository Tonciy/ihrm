package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.company.response.ProfileResult;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.entity.UserResult;
import cn.zeroeden.exception.CommonException;
import cn.zeroeden.system.service.UserService;
import cn.zeroeden.utils.JwtUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.zeroeden.entity.ResultCode.MOBILE_OR_PASSWORD_ERROR;

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

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录接口
     *
     * @param data 装载手机号和密码的载体
     * @return 返回结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> data) {
        String mobile = (String) data.get("mobile");
        String password = (String) data.get("password");
        User user = userService.findByMobile(mobile);
        if (user == null || !user.getPassword().equals(password)) {
            // 登录失败
            return new Result(MOBILE_OR_PASSWORD_ERROR);
        } else {
            // 登录成功
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("companyId", user.getCompanyId());
            map.put("companyName", user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return Result.SUCCESS(token);
        }
    }

    /**
     * 用户登录后获取其权限信息
     *
     * @param request 请求
     * @return
     */
    @PostMapping("/profile")
    public Result profile(HttpServletRequest request) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            throw new CommonException("未登录");
        }
        String token = authorization.replace("Bearer ", "");
        Claims claims = jwtUtils.parseJwt(token);
        String userId = claims.getId();
        User user = userService.findById(userId);
        return Result.SUCCESS(new ProfileResult(user));
    }

    @PostMapping("/user")
    public Result save(@RequestBody User user) {
        // 1. 设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.add(user);
        return Result.SUCCESS();
    }

    /**
     * 根据用户
     *
     * @return
     */
    @GetMapping("/user")
    public Result findAll(int page, int size, @RequestParam Map map) {
        //TODO 假设当前用户的企业id
        map.put("companyId", companyId);
        IPage res = userService.findAll(map, page, size);
        PageResult<User> r = new PageResult<>(res.getTotal(), res.getRecords());
        return new Result(ResultCode.SUCCESS, r);
    }

    /**
     * 根据id查询user
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public Result findById(@PathVariable String id) {
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    /**
     * 根据id修改user
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/user/{id}")
    public Result updateById(@PathVariable String id, @RequestBody User user) {
        // 设置部门的id
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public Result deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 为用户分配角色
     *
     * @param map 装载了用户id和待分配的角色ids
     * @return
     */
    @PutMapping("/user/assignRoles")
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        String id = (String) map.get("id");
        List<String> roleIds = (List<String>) map.get("ids");
        userService.assignRoles(id, roleIds);
        return Result.SUCCESS();
    }
}
