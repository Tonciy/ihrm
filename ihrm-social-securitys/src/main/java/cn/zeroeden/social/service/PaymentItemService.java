package cn.zeroeden.social.service;

import cn.zeroeden.domain.socialSecuritys.CityPaymentItem;
import cn.zeroeden.domain.socialSecuritys.PaymentItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PaymentItemService extends IService<PaymentItem> {

    /**
     * 根据城市id去获取此城市的参保项目
     * @param cityId 城市id
     * @return 此城市的参保项目集合
     */
    List<CityPaymentItem> findAllByCityId(String cityId);

}
