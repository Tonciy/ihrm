package cn.zeroeden.system.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.company.response.ProfileResult;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.entity.UserResult;
import cn.zeroeden.system.client.DepartmentFeignClient;
import cn.zeroeden.system.service.PermissionService;
import cn.zeroeden.system.service.UserService;
import cn.zeroeden.utils.JwtUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @PostMapping("/user/upload/{id}")
    public Result upload(@PathVariable String id, @RequestParam(name = "file") MultipartFile file) throws IOException {
        String imgUrl = userService.uploadImage(id, file);
        return Result.SUCCESS(imgUrl);
    }

    /**
     * 通过Excel，添加用户
     *
     * @param file Excel文件
     * @return
     */
    @PostMapping("/user/import")
    public Result importUserByExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<User> users = new ArrayList<>();
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Object[] values = new Object[row.getLastCellNum()];
            for (int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum);
                Object value = getCellValue(cell);
                values[cellNum] = value;
            }
            User user = new User(values);
            users.add(user);

        }
        userService.saveAll(users, companyId, companyName);
        return Result.SUCCESS();
    }

    /**
     * 从Cell中获取数据
     */
    private  Object getCellValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case STRING: //字符串类型
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: //boolean类型
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC: //数字类型（包含日期和普通数字）
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //公式类型
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 测试远程调用
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public Result testFeign(@PathVariable(value = "id") String id) throws Exception {
        Result result = departmentFeignClient.findById(id);
        return result;
    }

    /**
     * 登录接口
     *
     * @param data 装载手机号和密码的载体
     * @return 返回结果
     */
    @PostMapping(value = "/login", name = "point-user-delete")
    public Result login(@RequestBody Map<String, Object> data) {
        String mobile = (String) data.get("mobile");
        String password = (String) data.get("password");
        // 改为使用Shiro框架
        try {
            // 加密密码
            password = new Md5Hash(password, mobile, 3).toString();
            // 构造token
            UsernamePasswordToken token = new UsernamePasswordToken(mobile, password);
            // 获取Subject
            Subject subject = SecurityUtils.getSubject();
            // 调用login方法，进入realm中完成认证
            subject.login(token);
            // 获取sessionId
            String sessionId = subject.getSession().getId().toString();
            return Result.SUCCESS(sessionId);
        } catch (Exception e) {
            // 登录失败
            return new Result(MOBILE_OR_PASSWORD_ERROR);
        }


        // 下面是正常的token执行逻辑
//        User user = userService.findByMobile(mobile);
//        if (user == null || !user.getPassword().equals(password)) {
//            // 登录失败
//            return new Result(MOBILE_OR_PASSWORD_ERROR);
//        } else {
//            // 登录成功
//            StringBuilder sb = new StringBuilder();
//            // 获取当前用户可以访问的所有API权限
//            Set<Role> roles = user.getRoles();
//            for (Role role : roles) {
//                for (Permission permission : role.getPermissions()) {
//                    if(permission.getType() == PermissionConstants.PY_API){
//                        // code是每个API请求的唯一标识符
//                        sb.append(permission.getCode()).append(",");
//                    }
//                }
//            }
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("companyId", user.getCompanyId());
//            map.put("companyName", user.getCompanyName());
//            map.put("apis",sb.toString());
//            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
//            return Result.SUCCESS(token);
//        }
    }

    /**
     * 用户登录后获取其权限信息
     *
     * @param request 请求
     * @return
     */
    @PostMapping("/profile")
    public Result profile(HttpServletRequest request) throws Exception {
        // shiro方式获取
        // 获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        // subject 获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        // 获取安全数据
        ProfileResult profileResult = (ProfileResult) principals.getPrimaryPrincipal();

        // jwt方式获取
//        String userId = claims.getId();
//        User user = userService.findById(userId);
//        ProfileResult profileResult = null;
//        if (USER.equals(user.getLevel())) {
//            // 普通用户
//            profileResult = new ProfileResult(user);
//        } else {
//            HashMap<String, Object> map = new HashMap<>();
//            if (COMPANY_ADMIN.equals(user.getLevel())) {
//                // 企业管理员
//                map.put("enVisible", "1");
//            }
//            List<Permission> list = permissionService.findAll(map);
//            profileResult = new ProfileResult(user, list);
//        }
        return Result.SUCCESS(profileResult);
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
        List<User> users = res.getRecords();
        for (int i = 0; i < users.size(); i++) {
            users.set(i, userService.findRoles(users.get(i)));
        }
        PageResult<User> r = new PageResult<>(res.getTotal(), users);
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
