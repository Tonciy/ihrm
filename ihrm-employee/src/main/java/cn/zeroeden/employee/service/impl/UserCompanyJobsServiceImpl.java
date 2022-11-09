package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.UserCompanyJobs;
import cn.zeroeden.employee.dao.UserCompanyJobsDao;
import cn.zeroeden.employee.service.UserCompanyJobsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/10/19 9:52
 * Description:
 */
@Service
public class UserCompanyJobsServiceImpl extends ServiceImpl<UserCompanyJobsDao,UserCompanyJobs> implements UserCompanyJobsService {
    @Autowired
    private UserCompanyJobsDao userCompanyJobsDao;

    @Override
    public void mySave(UserCompanyJobs jobsInfo) {
        userCompanyJobsDao.insert(jobsInfo);
    }

    @Override
    public UserCompanyJobs findById(String userId) {
        return userCompanyJobsDao.findByUserId(userId);
    }
}
