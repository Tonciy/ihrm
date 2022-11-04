package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {
    /**
     * 添加角色
     * @param role 角色信息实体
     */
     void add(Role role);
    /**
     * 更新角色
     * @param role 角色信息实体
     */
     void update(Role role);

    /**
     * 根据id查询角色
     * @param id 角色id
     * @return 此id角色
     */
     Role findById(String id);
    /**
     * 根据id删除角色
     * @param id 角色id
     */
     void delete(String id);

    /**
     * 分页查询角色信息
     * @param roleId 角色id
     * @param page 当前页
     * @param size 一页数量
     * @return 分页
     */
     IPage<Role> findSearch(String roleId, int page, int size) ;

    /**
     * 为角色赋予权限
     * @param roleId  角色id
     * @param permissionIds 权限id集合
     */
     void assignRoles(String roleId, List<String> permissionIds);

    /**
     * 查询所有角色信息
     * @return 角色集合
     */
    List<Role> findAll();
}
