package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.UserCompanyJobs;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserCompanyJobsService extends IService<UserCompanyJobs> {

     void mySave(UserCompanyJobs jobsInfo);

     UserCompanyJobs findById(String userId);
}
