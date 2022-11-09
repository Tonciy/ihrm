package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.EmployeeTransferPosition;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TransferPositionService extends IService<EmployeeTransferPosition> {

     EmployeeTransferPosition findById(String uid, Integer status);

     void mySave(EmployeeTransferPosition transferPosition);
}
