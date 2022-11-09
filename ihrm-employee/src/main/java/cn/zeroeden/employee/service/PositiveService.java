package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.EmployeePositive;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PositiveService extends IService<EmployeePositive> {


    EmployeePositive findById(String uid, Integer status);

    EmployeePositive findById(String uid);

    void mySave(EmployeePositive positive);
}
