package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.EmployeePositive;
import cn.zeroeden.employee.dao.PositiveDao;
import cn.zeroeden.employee.service.PositiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/10/22 15:23
 * Description:
 */
@Service
public class PositiveServiceImpl extends ServiceImpl<PositiveDao, EmployeePositive> implements PositiveService {
    @Autowired
    private PositiveDao positiveDao;

    @Override
    public EmployeePositive findById(String uid, Integer status) {
        EmployeePositive positive = positiveDao.findByUserId(uid);
        if (status != null && positive != null) {
            if (positive.getEstatus() != status) {
                positive = null;
            }
        }
        return positive;
    }

    @Override
    public EmployeePositive findById(String uid) {
        return positiveDao.findByUserId(uid);
    }

    @Override
    public void mySave(EmployeePositive positive) {
        positive.setCreateTime(new Date());
        positive.setEstatus(1);//未执行
        positiveDao.insert(positive);
    }
}
