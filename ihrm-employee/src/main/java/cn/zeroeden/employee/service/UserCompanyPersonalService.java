package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import cn.zeroeden.domain.employee.response.EmployeeReportResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserCompanyPersonalService extends IService<UserCompanyPersonal> {

     void mySave(UserCompanyPersonal personalInfo);

     UserCompanyPersonal findById(String userId) ;


     /**
      * 根据公司id和年月查出相关人员信息
      * @param companyId 公司id
      * @param month 年月  2019-03
      * @return
      */
     List<EmployeeReportResult> findByReport(String companyId, String month);
}
