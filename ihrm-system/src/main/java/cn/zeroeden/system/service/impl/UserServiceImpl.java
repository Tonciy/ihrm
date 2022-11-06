package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.system.Role;
import cn.zeroeden.domain.system.User;
import cn.zeroeden.domain.system.UserRole;
import cn.zeroeden.system.dao.RoleDao;
import cn.zeroeden.system.dao.UserDao;
import cn.zeroeden.system.dao.UserRoleDao;
import cn.zeroeden.system.service.UserService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        user.setId(id);
        user.setEnableState(1); // 1为启用
        user.setPassword("123456"); // 初始密码
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
        if(user != null){
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
        User user =  userDao.selectById(id);
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
     *
     * @param map  包含的查询条件   hasDept, departmentId, companyId
     * @param page 当前页码
     * @param size  页面数据量
     * @return
     */
    @Override
    public IPage<User> findAll(Map<String, Object> map, int page, int size) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
       if(!StringUtils.isEmpty(map.get("companyId"))){
           queryWrapper.eq(User::getCompanyId, map.get("companyId"));
       }
        if(!StringUtils.isEmpty(map.get("departmentId"))){
            queryWrapper.eq(User::getDepartmentId, map.get("departmentId"));
        }
        if(!StringUtils.isEmpty(map.get("hasDept"))){
            if( StringUtils.isEmpty(map.get("hasDept")) || "0".equals((String)map.get("hasDept"))){
                // 没有部门限制
                queryWrapper.isNull(User::getDepartmentId);
            }else{
                // 有部门限制
                queryWrapper.isNotNull(User::getDepartmentId);
            }
        }

        IPage<User> myPage = new Page<>(page, size);
        myPage = userDao.selectPage(myPage, queryWrapper );
        return myPage;
    }

    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }
}
