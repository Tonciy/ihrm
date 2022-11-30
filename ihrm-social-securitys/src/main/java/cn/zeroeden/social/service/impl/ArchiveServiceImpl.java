package cn.zeroeden.social.service.impl;

import cn.zeroeden.domain.employee.UserCompanyPersonal;
import cn.zeroeden.domain.socialSecuritys.Archive;
import cn.zeroeden.domain.socialSecuritys.ArchiveDetail;
import cn.zeroeden.domain.socialSecuritys.CityPaymentItem;
import cn.zeroeden.domain.socialSecuritys.UserSocialSecurity;
import cn.zeroeden.entity.Result;
import cn.zeroeden.social.client.EmployeeFeignClient;
import cn.zeroeden.social.dao.ArchiveDao;
import cn.zeroeden.social.dao.ArchiveDetailDao;
import cn.zeroeden.social.dao.UserSocialSecurityDao;
import cn.zeroeden.social.service.ArchiveService;
import cn.zeroeden.social.service.PaymentItemService;
import cn.zeroeden.social.service.UserSocialService;
import cn.zeroeden.utils.IdWorker;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArchiveServiceImpl extends ServiceImpl<ArchiveDao, Archive> implements ArchiveService {


    @Autowired
    private ArchiveDao archiveDao;

    @Autowired
    private ArchiveDetailDao archiveDetailDao;

    @Autowired
    private EmployeeFeignClient employeeFeignClient;

    @Autowired
    private UserSocialService userSocialService;

    @Autowired
    private PaymentItemService paymentItemService;

    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;


    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private IdWorker idWorker;
    @Override
    public Archive findArchiveByCompanyIDAndYearMonth(String companyId, String yearMonth) {
        LambdaQueryWrapper<Archive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Archive::getCompanyId, companyId);
        queryWrapper.eq(Archive::getYearsMonth, yearMonth);
        Archive archive = archiveDao.selectOne(queryWrapper);
        return archive;
    }

    @Override
    public List<ArchiveDetail> findAllDetailByArchiveId(String id) {
        LambdaQueryWrapper<ArchiveDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveDetail::getArchiveId, id);
        List<ArchiveDetail> archives = archiveDetailDao.selectList(queryWrapper);
        return archives;
    }


    @Override
    public List<ArchiveDetail> getReports(String companyId, String yearMonth) {
        // 1. 先查询某个公司的所有员工社保新消息--一个map就是一个员工
        List<Map> userSocialSecurityItemPage = userSocialSecurityDao.getAll(companyId);

        List<ArchiveDetail> list = new ArrayList<>();

        // 2. 对每个员工设置一些个人信息以及社保相关信息
        for (Map map : userSocialSecurityItemPage) {
            String userId = (String)map.get("userId");
            String mobile = (String)map.get("mobile");
            String username = (String)map.get("username");
            String departmentName = (String)map.get("departmentName");
            ArchiveDetail vo = new ArchiveDetail(userId,mobile,username,departmentName);
            vo.setTimeOfEntry((Date) map.get("timeOfEntry"));
            //获取个人信息
            Result personalResult = employeeFeignClient.findPersonalInfo(vo.getUserId());
            if (personalResult.isSuccess()) {
                UserCompanyPersonal userCompanyPersonal = JSON.parseObject(JSON.toJSONString(personalResult.getData()), UserCompanyPersonal.class);
                vo.setUserCompanyPersonal(userCompanyPersonal);
            }
            //社保相关信息
            getOtherData(vo, yearMonth);
            list.add(vo);
        }
        return list;
    }

    @Override
    public void archive(String companyId, String yearMonth) {
        // 1. 查询此公司此年月的所有用户社保明细信息
        List<ArchiveDetail> data = this.getReports(companyId, yearMonth);
        // 用来计算当月的企业和员工的所有社保支出金额
        BigDecimal enterpriseMoney = new BigDecimal(0);
        BigDecimal personMoney  = new BigDecimal(0);
        for (ArchiveDetail person : data) {
            BigDecimal t1 = person.getProvidentFundEnterprises() == null? new BigDecimal(0): person.getProvidentFundEnterprises();
            BigDecimal t2 = person.getSocialSecurityEnterprise() == null? new BigDecimal(0): person.getSocialSecurityEnterprise();
            BigDecimal t3 = person.getProvidentFundIndividual() == null? new BigDecimal(0): person.getProvidentFundIndividual();
            BigDecimal t4 = person.getSocialSecurityIndividual() == null? new BigDecimal(0): person.getSocialSecurityIndividual();
            enterpriseMoney = enterpriseMoney.add(t1).add(t2);
            personMoney = personMoney.add(t3).add(t4);
        }
        // 3. 计算当月是否已经归档
        Archive archive = archiveService.findArchiveByCompanyIDAndYearMonth(companyId, yearMonth);
        if(archive == null){
            // 此公司此年月还没做过了归档-添些基础信息
            archive = new Archive();
            archive.setCompanyId(companyId);
            archive.setYearsMonth(yearMonth);
            archive.setId(idWorker.nextId() + "");
            archive.setCreationTime(new Date());
        }
        // 设置保存或者更新后的企业和个人社保总支出
        archive.setEnterprisePayment(enterpriseMoney);
        archive.setPersonalPayment(personMoney);
        archive.setTotal(enterpriseMoney.add(personMoney));
        // 保存或者更新归档
        archiveDao.insert(archive);
        // 5. 保存员工的历史归档数据
        for (ArchiveDetail archiveDetail : data) {
            archiveDetail.setId(idWorker.nextId() + "");
            archiveDetail.setArchiveId(archive.getId());
            archiveDetailDao.insert(archiveDetail);

        }
    }

    public void getOtherData(ArchiveDetail vo, String yearMonth) {

        UserSocialSecurity userSocialSecurity = userSocialService.findById(vo.getUserId());
        if(userSocialSecurity == null) {
            return;
        }

        BigDecimal socialSecurityCompanyPay = new BigDecimal(0);

        BigDecimal socialSecurityPersonalPay = new BigDecimal(0);

        List<CityPaymentItem> cityPaymentItemList = paymentItemService.findAllByCityId(userSocialSecurity.getProvidentFundCityId());

        for (CityPaymentItem cityPaymentItem : cityPaymentItemList) {
            if (cityPaymentItem.getSwitchCompany()) {
                BigDecimal augend;
                if (cityPaymentItem.getPaymentItemId().equals("4") && userSocialSecurity.getIndustrialInjuryRatio() != null) {
                    augend = userSocialSecurity.getIndustrialInjuryRatio().multiply(userSocialSecurity.getSocialSecurityBase());
                } else {
                    augend = cityPaymentItem.getScaleCompany().multiply(userSocialSecurity.getSocialSecurityBase());
                }
                BigDecimal divideAugend = augend.divide(new BigDecimal(100));
                socialSecurityCompanyPay = socialSecurityCompanyPay.add(divideAugend);
            }
            if (cityPaymentItem.getSwitchPersonal()) {
                BigDecimal augend = cityPaymentItem.getScalePersonal().multiply(userSocialSecurity.getSocialSecurityBase());
                BigDecimal divideAugend = augend.divide(new BigDecimal(100));
                socialSecurityPersonalPay = socialSecurityPersonalPay.add(divideAugend);
            }
        }

        vo.setSocialSecurity(socialSecurityCompanyPay.add(socialSecurityPersonalPay));
        vo.setSocialSecurityEnterprise(socialSecurityCompanyPay);
        vo.setSocialSecurityIndividual(socialSecurityPersonalPay);
        vo.setUserSocialSecurity(userSocialSecurity);
        vo.setSocialSecurityMonth(yearMonth);
        vo.setProvidentFundMonth(yearMonth);
    }

}
