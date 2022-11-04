package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RoleService extends IService<Role> {
    /**
     * 添加角色
     */
    public void add(Role role);
    /**
     * 更新角色
     */
    public void update(Role role);

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    public Role findById(String id);
    /**
     * 根据id删除角色
     */
    public void delete(String id);

    /**
     * 分页查询角色信息
     * @param companyId
     * @param page
     * @param size
     * @return
     */
    public IPage<Role> findSearch(String companyId, int page, int size) ;
}
