package cn.zeroeden.domain.salarys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("sa_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings implements Serializable {
    private static final long serialVersionUID = 4570110025510021036L;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 对应社保自然月
     */
    private Integer socialSecurityType;
    /**
     * 津贴方案名称
     */
    private String subsidyName;
    /**
     * 津贴备注
     */
    private String subsidyRemark;
    /**
     * 交通补贴计算类型
     */
    private Integer transportationSubsidyScheme;
    /**
     * 交通补贴金额
     */
    private BigDecimal transportationSubsidyAmount;
    /**
     * 通讯补贴计算类型
     */
    private Integer communicationSubsidyScheme;
    /**
     * 通讯补贴金额
     */
    private BigDecimal communicationSubsidyAmount;
    /**
     * 午餐补贴计算类型
     */
    private Integer lunchAllowanceScheme;
    /**
     * 午餐补贴金额
     */
    private BigDecimal lunchAllowanceAmount;
    /**
     * 住房补助计算类型
     */
    private Integer housingSubsidyScheme;
    /**
     * 住房补助金额
     */
    private BigDecimal housingSubsidyAmount;
    /**
     * 计税方式
     */
    private Integer taxCalculationType;
}
