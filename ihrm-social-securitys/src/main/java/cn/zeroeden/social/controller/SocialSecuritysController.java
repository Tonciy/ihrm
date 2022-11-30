package cn.zeroeden.social.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.socialSecuritys.CityPaymentItem;
import cn.zeroeden.domain.socialSecuritys.CompanySettings;
import cn.zeroeden.domain.socialSecuritys.UserSocialSecurity;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.social.client.SystemFeignClient;
import cn.zeroeden.social.service.CompanySettingsService;
import cn.zeroeden.social.service.PaymentItemService;
import cn.zeroeden.social.service.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2022/11/27
 * @description:
 */

@RestController
@RequestMapping("/social_securitys")
public class SocialSecuritysController extends BaseController {


    @Autowired
    private CompanySettingsService companySettingsService;

    @Autowired
    private UserSocialService userSocialService;

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Autowired
    private PaymentItemService paymentItemService;
    /**
     * 获取企业某个年月是否做了社保
     * @return
     */
    @GetMapping("/settings")
    public Result settings() {
        CompanySettings cs = companySettingsService.findById(companyId);
        return Result.SUCCESS(cs);
    }


    /**
     * 为企业某个年月做社保
     *
     * @return
     */
    @PostMapping("/settings")
    public Result saveSettings(@RequestBody CompanySettings companySettings) {
        companySettingsService.mySave(companySettings);
        return Result.SUCCESS();
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map map) {
        // 获取分页参数
        Integer page = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");
        PageResult rs = userSocialService.findAll(page, pageSize, companyId);
        return Result.SUCCESS(rs);
    }

    /**
     * 根据用户id查询用户基本信息以及社保数据
     * @param id 用户id
     * @return 装载用户基本信息及社保数据
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        Map<String,Object> map = new HashMap();
        // 查询用户基本信息
        Object obj = systemFeignClient.findById(id).getData();
        map.put("user", obj);
        // 查询用户社保数据
        UserSocialSecurity uss = userSocialService.findById(id);
        map.put("userSocialSecurity", uss);
        return Result.SUCCESS(map);
    }

    /**
     * 根据城市id去获取此城市的参保项目集合
     * @param id 城市id
     * @return 此城市的参保项目集合
     */
    @GetMapping("/payment_item/{id}")
    public Result findPaymentItemById(@PathVariable String id){
        List<CityPaymentItem> list = paymentItemService.findAllByCityId(id);
        return Result.SUCCESS(list);
    }

    /**
     * 去保存/更新用户的社保资料
     * @param uss 用户的社保资料
     * @return 统一返回结果
     */
    @PutMapping("/{id}")
    public Result saveUserSocailSecurity(@RequestBody UserSocialSecurity uss){
        userSocialService.save(uss);
        return Result.SUCCESS();
    }

}
