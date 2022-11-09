package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.EmployeeTransferPosition;
import cn.zeroeden.employee.dao.TransferPositionDao;
import cn.zeroeden.employee.service.TransferPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/10/22 10:57
 * Description:
 */
@Service
public class TransferPositionServiceImpl extends ServiceImpl<TransferPositionDao,EmployeeTransferPosition> implements TransferPositionService {
    @Autowired
    private TransferPositionDao transferPositionDao;

    @Override
    public EmployeeTransferPosition findById(String uid, Integer status) {
        EmployeeTransferPosition transferPosition = transferPositionDao.findByUserId(uid);
        if (status != null && transferPosition != null) {
            if (transferPosition.getEstatus() != status) {
                transferPosition = null;
            }
        }
        return transferPosition;
    }

    @Override
    public void mySave(EmployeeTransferPosition transferPosition) {
        transferPosition.setCreateTime(new Date());
        transferPosition.setEstatus(1); //未执行
        transferPositionDao.insert(transferPosition);
    }
}
