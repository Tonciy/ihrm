package cn.zeroeden.social.service.impl;

import cn.zeroeden.domain.socialSecuritys.UserSocialSecurity;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.social.dao.UserSocialSecurityDao;
import cn.zeroeden.social.service.UserSocialService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserSocialServiceImpl implements UserSocialService {


    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;

    @Override
    public PageResult findAll(Integer page, Integer pageSize, String companyId) {
        List<Map> data = userSocialSecurityDao.findPage(companyId, (page - 1) * pageSize, pageSize);
        Long total = userSocialSecurityDao.count(companyId);
        return new PageResult(total, data);
    }

    @Override
    public UserSocialSecurity findById(String id) {
        UserSocialSecurity uss = userSocialSecurityDao.selectById(id);
        return uss;
    }

    @Override
    public void save(UserSocialSecurity uss) {
        // 先看是否有记录先
        LambdaQueryWrapper<UserSocialSecurity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSocialSecurity::getUserId, uss.getUserId());
        Integer count = userSocialSecurityDao.selectCount(queryWrapper);
        if(count == 0){
            // 无数据，插入数据
            userSocialSecurityDao.insert(uss);
        }else{
            // 更新数据
            userSocialSecurityDao.update(uss, queryWrapper);
        }

    }
}
