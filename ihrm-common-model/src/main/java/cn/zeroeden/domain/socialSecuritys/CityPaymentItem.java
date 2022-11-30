package cn.zeroeden.domain.socialSecuritys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@TableName("ss_city_payment_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityPaymentItem implements Serializable {
    private static final long serialVersionUID = -2676589979027211627L;
    private String id;
    /**
     * 城市id
     */
    private String cityId;
    /**
     * 缴费项目id
     */
    private String paymentItemId;
    /**
     * 企业是否缴纳开关，0为禁用，1为启用
     */
    private Boolean switchCompany;
    /**
     * 企业比例
     */
    private BigDecimal scaleCompany;
    /**
     * 个人是否缴纳开关，0为禁用，1为启用
     */
    private Boolean switchPersonal;
    /**
     * 个人比例
     */
    private BigDecimal scalePersonal;
    /**
     * 缴费项目名称
     */
    private String name;
}
