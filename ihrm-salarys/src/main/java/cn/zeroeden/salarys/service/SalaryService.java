package cn.zeroeden.salarys.service;

import cn.zeroeden.domain.salarys.UserSalary;
import cn.zeroeden.entity.PageResult;

public interface SalaryService {


    /**
     * 定薪或者调薪
     * @param userSalary 载体
     */
     void saveUserSalary(UserSalary userSalary);

    /**
     * 查询用户薪资
     * @param userId 用户id
     * @return
     */
     UserSalary findUserSalary(String userId);


    /**
     * 分页查询当月薪资列表
     * @param page 当前页
     * @param pageSize 一页数量
     * @param companyId 公司id
     * @return 具体数据
     */
     PageResult findAll(Integer page, Integer pageSize, String companyId);
}
