package cn.zeroeden.company.service;

import cn.zeroeden.domain.company.Company;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Zero
 * @Description 描述此类
 */
public interface CompanyService extends IService<Company> {

    /**
     * 增加实体
     * @param company
     */
    public void add(Company company);
}
