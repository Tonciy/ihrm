package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    /**
     * 保存部门
     * @param user 用户信
     */
    public void add(User user);

    /**
     * 更新部门
     * @param user 用户信息
     */
    public void update(User user);

    /**
     * 根据id查询部门
     * @param id 用户id
     */
    public User findById(String id);

    /**
     * 查询所有部门信息
     */
    public IPage<User> findAll(Map<String, Object> map, int page, int size);

    /**
     * 根据id删除部门
     * @param id 用户id
     */
    public void  deleteById(String id);


    /**
     * 赋予用户角色
     * @param id  用户id
     * @param roleIds 角色id集合
     */
    public void assignRoles(String id, List<String> roleIds);
}
