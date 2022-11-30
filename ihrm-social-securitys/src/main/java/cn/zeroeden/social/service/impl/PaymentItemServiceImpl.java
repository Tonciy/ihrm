package cn.zeroeden.social.service.impl;

import cn.zeroeden.domain.socialSecuritys.CityPaymentItem;
import cn.zeroeden.domain.socialSecuritys.PaymentItem;
import cn.zeroeden.social.dao.CityPaymentItemDao;
import cn.zeroeden.social.dao.PaymentItemDao;
import cn.zeroeden.social.service.PaymentItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentItemServiceImpl extends ServiceImpl<PaymentItemDao, PaymentItem> implements PaymentItemService {

    @Autowired
    private CityPaymentItemDao cityPaymentItemDao;

    @Override
    public List<CityPaymentItem> findAllByCityId(String cityId) {
        LambdaQueryWrapper<CityPaymentItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CityPaymentItem::getId, cityId);
        List<CityPaymentItem> list = cityPaymentItemDao.selectList(queryWrapper);
        return list;
    }
}
