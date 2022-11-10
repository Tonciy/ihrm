package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import cn.zeroeden.domain.employee.response.EmployeeReportResult;
import cn.zeroeden.employee.dao.UserCompanyPersonalDao;
import cn.zeroeden.employee.service.UserCompanyPersonalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class UserCompanyPersonalServiceImpl extends ServiceImpl<UserCompanyPersonalDao,UserCompanyPersonal> implements UserCompanyPersonalService {
    @Autowired
    private UserCompanyPersonalDao userCompanyPersonalDao;


    @Override
    public List<EmployeeReportResult> findByReport(String companyId, String month) {
        return userCompanyPersonalDao.findByReport(companyId, month);
    }

    @Override
    public void mySave(UserCompanyPersonal personalInfo) {
        userCompanyPersonalDao.insert(personalInfo);
    }


    @Override
    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }
}
