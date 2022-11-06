package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.system.*;
import cn.zeroeden.system.dao.*;
import cn.zeroeden.system.service.PermissionService;
import cn.zeroeden.system.service.RoleService;
import cn.zeroeden.utils.IdWorker;
import cn.zeroeden.utils.PermissionConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private UserDao userDao;
    @Override
    public Role findUsers(Role role) {
        if(role != null){
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserRole::getRoleId, role.getId());
            List<UserRole> userRoles = userRoleDao.selectList(wrapper);
            Set<User> users = new HashSet<>();
            for (UserRole userRole : userRoles) {
                User user = userDao.selectById(userRole.getUserId());
                users.add(user);
            }
            role.setUsers(users);

        }
        return role;
    }

    @Override
    public Role findPermissions(Role role) {
        if(role != null){
            LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RolePermission::getRoleId, role.getId());
            List<RolePermission> userRoles = rolePermissionDao.selectList(wrapper);
            Set<Permission> permissions = new HashSet<>();
            for (RolePermission rolePermission : userRoles) {
                Permission user = permissionDao.selectById(rolePermission.getPermissionId());
                permissions.add(user);
            }
            role.setPermissions(permissions);

        }
        return role;
    }

    @Override
    public void add(Role role) {
        // 填充其他参数
        role.setId(idWorker.nextId() + "");
        roleDao.insert(role);
    }

    @Override
    public void update(Role role) {
        Role target = roleDao.selectById(role.getId());
        target.setDescription(role.getDescription());
        target.setName(role.getName());
        roleDao.updateById(target);
    }

    @Override
    public Role findById(String id) {
        Role role =  roleDao.selectById(id);
        role = this.findUsers(role);
        role = this.findPermissions(role);
        return role;
    }

    @Override
    public void delete(String id) {
        roleDao.deleteById(id);
    }

    @Override
    public IPage<Role> findSearch(String companyId, int page, int size) {
        IPage<Role> myPage = new Page<>(page, size);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(companyId)) {
            wrapper.eq(Role::getCompanyId, companyId);
        }
        myPage = roleDao.selectPage(myPage, wrapper);
        return myPage;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleDao.selectList(null);
        for (int i = 0; i < roles.size(); i++) {
            Role role1 = roles.get(i);
            role1 = this.findUsers(role1);
            role1 = this.findPermissions(role1);
            roles.set(i, role1);
        }
        return roles;
    }

    @Override
    public void assignRoles(String roleId, List<String> permissionIds) {
        for (String permissionId : permissionIds) {
            // 查询pid是此权限的API权限列表
            List<Permission> list = permissionService.findByTypeAndPid(PermissionConstants.PY_API, permissionId);
            if(CollectionUtils.isEmpty(list)){
                // 在中间表添加角色id和 当前权限id的子API权限的id（permissionId可能为某个菜单栏或者按钮，旗下或许有多个子API资源）
                for (Permission permission : list) {
                    RolePermission tem = new RolePermission(roleId, permission.getId());
                    // 去重--防止有人模拟请求
                    LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(RolePermission::getRoleId, roleId);
                    wrapper.eq(RolePermission::getPermissionId, permission.getId());
                    Integer count = rolePermissionDao.selectCount(wrapper);
                    if(count == 0){
                        rolePermissionDao.insert(tem);
                    }

                }
            }
            // 在中间表添加角色id与当前权限id
            rolePermissionDao.insert(new RolePermission(roleId, permissionId));
        }
    }
}
