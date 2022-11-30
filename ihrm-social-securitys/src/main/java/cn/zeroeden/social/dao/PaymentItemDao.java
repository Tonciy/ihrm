package cn.zeroeden.social.dao;

import cn.zeroeden.domain.socialSecuritys.PaymentItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentItemDao extends BaseMapper<PaymentItem> {
}
