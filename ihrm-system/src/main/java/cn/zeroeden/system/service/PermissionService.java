package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {
    /**
     * 保存部门
     * @param map
     */
    public void add(Map<String,Object> map) throws Exception;

    /**
     * 更新部门
     * @param map
     */
    public void update(Map<String, Object> map) throws Exception;

    /**
     * 根据id查询部门
     * @param id
     */
    public Map<String, Object> findById(String id) throws Exception;

    /**
     * 查询所有部门信息
     */
    public List<Permission> findAll(Map<String, Object> map);

    /**
     * 根据id删除部门
     * @param id
     */
    public void  deleteById(String id) throws Exception;
}
