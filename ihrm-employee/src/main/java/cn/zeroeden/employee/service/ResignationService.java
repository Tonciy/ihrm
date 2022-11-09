package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.EmployeeResignation;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ResignationService extends IService<EmployeeResignation> {


     void mySave(EmployeeResignation resignation) ;

     EmployeeResignation findById(String userId) ;
}
