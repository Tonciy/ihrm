package cn.zeroeden.company.service.impl;

import cn.zeroeden.company.dao.CompanyDao;
import cn.zeroeden.company.service.CompanyService;
import cn.zeroeden.domain.company.Company;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zero
 * @Description 描述此类
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyDao, Company> implements CompanyService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CompanyDao companyDao;

    @Transactional
    @Override
    public void add(Company company) {
        // 1. 基本属性设置
        company.setId(String.valueOf(idWorker.nextId()));
        // 2. 设置默认状态
        // 0:未审核  1: 审核
        company.setAuditState("0");
        // 0:未激活  1: 激活
        company.setState(1);
       companyDao.insert(company);
    }



}
