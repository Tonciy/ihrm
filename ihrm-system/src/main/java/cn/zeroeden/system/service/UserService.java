package cn.zeroeden.system.service;

import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    /**
     * 根据手机号查找用户
     * @param mobile 手机号
     * @return 用户
     */
    User findByMobile(String mobile);

    /**
     * 保存部门
     * @param user 用户信
     */
     void add(User user);

    /**
     * 更新部门
     * @param user 用户信息
     */
     void update(User user);

    /**
     * 根据id查询部门
     * @param id 用户id
     */
     User findById(String id);

    /**
     * 查询所有部门信息
     */
     IPage<User> findAll(Map<String, Object> map, int page, int size);

    /**
     * 根据id删除部门
     * @param id 用户id
     */
     void  deleteById(String id);


    /**
     * 赋予用户角色
     * @param id  用户id
     * @param roleIds 角色id集合
     */
     void assignRoles(String id, List<String> roleIds);
}
