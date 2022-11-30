package cn.zeroeden.domain.socialSecuritys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@TableName("ss_archive")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archive implements Serializable {

    private static final long serialVersionUID = -7883369133555989567L;

    public Archive(String companyId, String yearMonth) {
        this.companyId = companyId;
        this.yearsMonth = yearMonth;
    }


    private String id;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 年月
     */
    private String yearsMonth;
    /**
     * 创建时间
     */
    private Date creationTime;
    /**
     * 企业缴费
     */
    private BigDecimal enterprisePayment;
    /**
     * 个人缴费
     */
    private BigDecimal personalPayment;
    /**
     * 合计
     */
    private BigDecimal total;
}
