package cn.zeroeden.social.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.socialSecuritys.*;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.entity.Result;
import cn.zeroeden.social.client.SystemFeignClient;
import cn.zeroeden.social.dao.CompanySettingsDao;
import cn.zeroeden.social.service.ArchiveService;
import cn.zeroeden.social.service.CompanySettingsService;
import cn.zeroeden.social.service.PaymentItemService;
import cn.zeroeden.social.service.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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


    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private CompanySettingsDao companySettingsDao;
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

    /**
     * 查询某个公司年月份数据报表
     * @param yearMonth 年月
     * @param opType 类型 1：归档 其他： 未归档
     * @return 装载数据报表
     */
    @GetMapping("/historys/{yearMonth}")
    public Result historyDetail(@PathVariable String yearMonth, int opType){
        List<ArchiveDetail> list = new ArrayList<>();
        if(opType == 1){
            // 查询未归档的数据
            // 查询此公司的此年月应该生成的数据报表
            list = archiveService.getReports(companyId, yearMonth);
        }else{
            // 查询已归档的数据
            // 先根据公司id和年月查询 归档总结历史数据
            Archive archive = archiveService.findArchiveByCompanyIDAndYearMonth(companyId, yearMonth);
            // 如果归档历史存在，就查询明细
            if(archive  != null){
                list = archiveService.findAllDetailByArchiveId(archive.getId());
            }
        }
        return Result.SUCCESS(list);
    }

    /**
     * 对某个公司的某年月的员工社保数据进行归档
     * @param yearMonth 年月
     * @return 无
     * @throws Exception 无
     */
    @PutMapping("/historys/{yearMonth}/archive")
    public Result historyDetail(@PathVariable String  yearMonth) throws Exception{
        archiveService.archive(companyId, yearMonth);
        return Result.SUCCESS();
    }

    /**
     * 为某个公司制作/切换报表周期
     * @param yearMonth 年月
     * @return 无
     */
    @PutMapping("/historys/{yearMonth}/newReport")
    public Result saveSettings(@PathVariable String yearMonth){
        // 获取到企业当前的年月报表进度
        CompanySettings cs = companySettingsService.findById(companyId);
        boolean flag = false;
        if(cs == null){
            cs = new CompanySettings();
            flag = true;
        }
        cs.setCompanyId(companyId);
        cs.setDataMonth(yearMonth);
        if(flag){
            // 这企业还没开始做报表，插入数据
            companySettingsDao.insert(cs);
        }else{
            // 这且有之前就已经有做报表了，更新数据为下一个月
            companySettingsDao.updateById(cs);
        }
        return Result.SUCCESS();

    }

    /**
     * 获取某个公司的某年的历史每个月的社保总归档数据
     * @param year 年份
     * @return 社保总归档数据集合
     * @throws Exception 统一处理
     */
    @GetMapping("/historys/{year}/list")
    public Result historyList(@PathVariable String year) throws Exception{
        List<Archive> list = archiveService.findArchiveByYearAndCompnayId(companyId, year);
        return Result.SUCCESS(list);
    }

    /**
     * 根据用户id和年月查询此用户在某年月的社保归档数据明细
     * @param userId 用户id
     * @param yearMonth 年月
     * @return 此用户在某年月的社保归档数据明细
     */
    @GetMapping("/historys/data")
    public Result histroysData(String userId,String yearMonth){
        ArchiveDetail data = archiveService.findUserArchiveDetailByUserIdAndYearMonth(userId, yearMonth);
        return Result.SUCCESS(data);
    }

}
