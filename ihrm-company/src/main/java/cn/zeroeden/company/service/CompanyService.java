package cn.zeroeden.company.service;

import cn.zeroeden.domain.company.Company;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Zero
 * @Description 描述此类
 */
public interface CompanyService extends IService<Company> {

    /**
     * 保存部门
     * @param department
     */
    public void add(Company department);


}
