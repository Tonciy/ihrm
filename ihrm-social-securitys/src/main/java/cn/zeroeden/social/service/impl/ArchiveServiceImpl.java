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
        // 1. ???????????????????????????????????????????????????--??????map??????????????????
        List<Map> userSocialSecurityItemPage = userSocialSecurityDao.getAll(companyId);

        List<ArchiveDetail> list = new ArrayList<>();

        // 2. ???????????????????????????????????????????????????????????????
        for (Map map : userSocialSecurityItemPage) {
            String userId = (String)map.get("userId");
            String mobile = (String)map.get("mobile");
            String username = (String)map.get("username");
            String departmentName = (String)map.get("departmentName");
            ArchiveDetail vo = new ArchiveDetail(userId,mobile,username,departmentName);
            vo.setTimeOfEntry((Date) map.get("timeOfEntry"));
            //??????????????????
            Result personalResult = employeeFeignClient.findPersonalInfo(vo.getUserId());
            if (personalResult.isSuccess()) {
                UserCompanyPersonal userCompanyPersonal = JSON.parseObject(JSON.toJSONString(personalResult.getData()), UserCompanyPersonal.class);
                vo.setUserCompanyPersonal(userCompanyPersonal);
            }
            //??????????????????
            getOtherData(vo, yearMonth);
            list.add(vo);
        }
        return list;
    }

    @Override
    public void archive(String companyId, String yearMonth) {
        // 1. ?????????????????????????????????????????????????????????
        List<ArchiveDetail> data = this.getReports(companyId, yearMonth);
        // ???????????????????????????????????????????????????????????????
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
        // 3. ??????????????????????????????
        Archive archive = archiveService.findArchiveByCompanyIDAndYearMonth(companyId, yearMonth);
        if(archive == null){
            // ???????????????????????????????????????-??????????????????
            archive = new Archive();
            archive.setCompanyId(companyId);
            archive.setYearsMonth(yearMonth);
            archive.setId(idWorker.nextId() + "");
            archive.setCreationTime(new Date());
        }
        // ????????????????????????????????????????????????????????????
        archive.setEnterprisePayment(enterpriseMoney);
        archive.setPersonalPayment(personMoney);
        archive.setTotal(enterpriseMoney.add(personMoney));
        // ????????????????????????
        archiveDao.insert(archive);
        // 5. ?????????????????????????????????
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


    @Override
    public List<Archive> findArchiveByYearAndCompnayId(String companyId, String year) {
        return archiveDao.findListByCompanyIdAndYear(companyId, year);
    }

    @Override
    public ArchiveDetail findUserArchiveDetailByUserIdAndYearMonth(String userId, String yearMonth) {
        LambdaQueryWrapper<ArchiveDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveDetail::getUserId, userId);
        queryWrapper.eq(ArchiveDetail::getYearsMonth, yearMonth);
        ArchiveDetail data = archiveDetailDao.selectOne(queryWrapper);
        return data;
    }
}
