package cn.zeroeden.salarys.service.impl;


import cn.zeroeden.domain.salarys.UserSalary;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.salarys.dao.UserSalaryDao;
import cn.zeroeden.salarys.service.SalaryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SalaryServiceImpl extends ServiceImpl<UserSalaryDao, UserSalary> implements SalaryService {
	
    @Autowired
    private UserSalaryDao userSalaryDao;

    /**
     * 定薪或者调薪
     *
     * @param userSalary 载体
     */
    @Override
    public void saveUserSalary(UserSalary userSalary) {
        userSalaryDao.insert(userSalary);
    }

    /**
     * 查询用户薪资
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public UserSalary findUserSalary(String userId) {
        LambdaQueryWrapper<UserSalary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSalary::getUserId, userId);
        return userSalaryDao.selectOne(queryWrapper);
    }

    /**
     * 分页查询当月薪资列表
     *
     * @param page      当前页
     * @param pageSize  一页数量
     * @param companyId 公司id
     * @return 具体数据
     */
    @Override
    public PageResult findAll(Integer page, Integer pageSize, String companyId) {

        List<Map> list = userSalaryDao.findPage(companyId, (page - 1) * pageSize, pageSize);
        int total = userSalaryDao.findPageGetTotal(companyId);
        return new PageResult((long)total,list);
    }
}
