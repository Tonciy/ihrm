package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.company.Department;
import cn.zeroeden.domain.system.Role;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.domain.system.UserRole;
import cn.zeroeden.system.client.DepartmentFeignClient;
import cn.zeroeden.system.dao.RoleDao;
import cn.zeroeden.system.dao.UserDao;
import cn.zeroeden.system.dao.UserRoleDao;
import cn.zeroeden.system.service.UserService;
import cn.zeroeden.system.utils.BaiduAiUtil;
import cn.zeroeden.utils.IdWorker;
import cn.zeroeden.utils.QiniuUploadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Zero
 * @time: 2022/11/2
 * @description:
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @Autowired
    private BaiduAiUtil baiduAiUtil;
    @Override
    public String uploadImage(String id, MultipartFile file) throws IOException {
        User user = userDao.selectById(id);
        // DataURL
//        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//        String encode = "data:image/" + type + ";base64,"+ Base64.encode(file.getBytes());
//        user.setStaffPhoto(encode);

        // 改用七牛云存储服务
        String key = new QiniuUploadUtil().upload(user.getId(), file.getBytes());
        if(key != null){
            user.setStaffPhoto(key);
        }
        userDao.updateById(user);

        // 将图片存到百度云人脸库中--后面可以人脸登录-没充钱没得用-。-
//        Boolean exist = baiduAiUtil.faceExist(id);
//        String imgBase64 = Base64Util.encode(file.getBytes());
//        if(exist){
//            // 更新
//            baiduAiUtil.faceUpdate(id, imgBase64);
//        }else{
//            // 注册
//            baiduAiUtil.faceRegister(id,imgBase64 );
//        }
        return key;
    }

    @Override
    @Transactional
    public void saveAll(List<User> users, String companyId, String companyName) throws Exception {
        for (User user : users) {
            // 基本设置
            user.setPassword(new Md5Hash("123456", user.getMobile(), 3).toString());
            user.setId(idWorker.nextId() + "");
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");
            Department department = departmentFeignClient.findByCode(user.getDepartmentId(), companyId);
            if(department != null){
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            this.save(user);
        }
    }

    @Autowired
    private UserDao userDao;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void add(User user) {
        // 设置默认值
        String id = idWorker.nextId() + "";
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        // 只能添加普通用户，管理员的添加在别的接口
        user.setLevel("user");
        user.setId(id);
        user.setEnableState(1); // 1为启用
        user.setPassword(password); // 初始密码
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        User target = userDao.selectById(user.getId());
        // 设置基本属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.updateById(target);
    }

    @Override
    public User findRoles(User user) {
        // 填充角色信息
        if (user != null) {
            String userId = user.getId();
            LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserRole::getUserId, userId);
            List<UserRole> roleIds = userRoleDao.selectList(queryWrapper);
            Set<Role> roles = new HashSet<>();
            for (UserRole roleId : roleIds) {
                Role role = roleDao.selectById(roleId.getRoleId());
                roles.add(role);
            }
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public User findByMobile(String mobile) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile, mobile);
        User user = userDao.selectOne(wrapper);
        user = this.findRoles(user);
        return user;
    }

    @Override
    public User findById(String id) {
        User user = userDao.selectById(id);
        // 填充角色信息
        user = this.findRoles(user);
        return user;
    }

    @Override
    public void assignRoles(String id, List<String> roleIds) {
        // 直接往中间表插入数据即可
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole(roleId, id);
            userRoleDao.insert(userRole);
        }
    }

    /**
     * @param map  包含的查询条件   hasDept, departmentId, companyId
     * @param page 当前页码
     * @param size 页面数据量
     * @return
     */
    @Override
    public IPage<User> findAll(Map<String, Object> map, int page, int size) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(map.get("companyId"))) {
            queryWrapper.eq(User::getCompanyId, map.get("companyId"));
        }
        if (!StringUtils.isEmpty(map.get("departmentId"))) {
            queryWrapper.eq(User::getDepartmentId, map.get("departmentId"));
        }
        if (!StringUtils.isEmpty(map.get("hasDept"))) {
            if (StringUtils.isEmpty(map.get("hasDept")) || "0".equals((String) map.get("hasDept"))) {
                // 没有部门限制
                queryWrapper.isNull(User::getDepartmentId);
            } else {
                // 有部门限制
                queryWrapper.isNotNull(User::getDepartmentId);
            }
        }

        IPage<User> myPage = new Page<>(page, size);
        myPage = userDao.selectPage(myPage, queryWrapper);
        return myPage;
    }

    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }
}
