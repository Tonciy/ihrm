package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.system.User;
import cn.zeroeden.system.dao.UserDao;
import cn.zeroeden.system.service.UserService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

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
    public User findById(String id) {
        return userDao.selectById(id);
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
