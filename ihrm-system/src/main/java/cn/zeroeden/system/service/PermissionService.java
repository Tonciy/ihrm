package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {
    /**
     * 保存权限
     *
     * @param map 装载了权限信息的载体
     */
    void add(Map<String, Object> map) throws Exception;

    /**
     * 更新权限
     *
     * @param map 装载了权限信息的载体
     */
    void update(Map<String, Object> map) throws Exception;

    /**
     * 根据id查询权限
     *
     * @param id 权限id
     * @return 具体权限信息
     */
    Map<String, Object> findById(String id) throws Exception;

    /**
     * 查询所有权限信息
     * @param map 查询条件
     * @return 权限信息集合
     */
    List<Permission> findAll(Map<String, Object> map);

    /**
     * 根据id删除权限
     *
     * @param id 权限id
     */
    void deleteById(String id) throws Exception;

    /**
     * 根据资源类型和pid查询权限
     *
     * @param type 资源类型
     * @param pid  资源父id
     * @return 具体权限信息
     */
    List<Permission> findByTypeAndPid(Integer type, String pid);
}
