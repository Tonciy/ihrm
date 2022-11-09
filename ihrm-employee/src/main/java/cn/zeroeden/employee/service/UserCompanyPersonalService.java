package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserCompanyPersonalService extends IService<UserCompanyPersonal> {

     void mySave(UserCompanyPersonal personalInfo);

     UserCompanyPersonal findById(String userId) ;
}
