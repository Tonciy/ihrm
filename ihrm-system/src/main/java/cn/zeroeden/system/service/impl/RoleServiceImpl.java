package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.system.Role;
import cn.zeroeden.system.dao.RoleDao;
import cn.zeroeden.system.service.RoleService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        return roleDao.selectById(id);
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
}
