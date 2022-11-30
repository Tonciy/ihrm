package cn.zeroeden.social.service;

import cn.zeroeden.domain.socialSecuritys.UserSocialSecurity;
import cn.zeroeden.entity.PageResult;

public interface UserSocialService  {
    /**
     * 用户社保情况分页查询
     * @param page 当前页
     * @param pageSize 一页展示数量
     * @param companyId 公司id
     * @return 分页数据
     */
    PageResult findAll(Integer page, Integer pageSize, String companyId);

    /**
     * 根据用户id查询其社保信息
     * @param id 用户id
     * @return 用户社保信息
     */
    UserSocialSecurity findById(String id);

    /**
     * 保存用户的社保资料
     * @param uss 用户社保资料
     */
    void save(UserSocialSecurity uss);
}
