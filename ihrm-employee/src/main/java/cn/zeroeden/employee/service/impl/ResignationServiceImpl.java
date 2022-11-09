package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.EmployeeResignation;
import cn.zeroeden.employee.dao.EmployeeResignationDao;
import cn.zeroeden.employee.service.ResignationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/10/20 14:23
 * Description:
 */
@Service
public class ResignationServiceImpl extends ServiceImpl<EmployeeResignationDao,EmployeeResignation> implements ResignationService {
    @Autowired
    EmployeeResignationDao resignationDao;

    @Override
    public void mySave(EmployeeResignation resignation) {
        resignation.setCreateTime(new Date());
        resignationDao.insert(resignation);
    }

    @Override
    public EmployeeResignation findById(String userId) {
        return resignationDao.findByUserId(userId);
    }
}
