package cn.zeroeden.domain.socialSecuritys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@TableName("ss_payment_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItem implements Serializable {
    private static final long serialVersionUID = -9055803869196919406L;
    //ID
    private String id;
    /**
     * 缴费项目名称
     */
    private String name;
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
}
