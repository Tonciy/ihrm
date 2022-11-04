package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface UserService extends IService<User> {

    /**
     * 保存部门
     * @param user
     */
    public void add(User user);

    /**
     * 更新部门
     * @param user
     */
    public void update(User user);

    /**
     * 根据id查询部门
     * @param id
     */
    public User findById(String id);

    /**
     * 查询所有部门信息
     */
    public IPage<User> findAll(Map<String, Object> map, int page, int size);

    /**
     * 根据id删除部门
     * @param id
     */
    public void  deleteById(String id);
}
