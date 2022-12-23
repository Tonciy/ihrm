package cn.zeroeden.domain.salarys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("sa_company_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettings implements Serializable {
    private static final long serialVersionUID = -2620680846592406186L;
    /**
     * 企业id
     */

    private String companyId;
    /**
     * 是否设置 0为未设置，1为已设置
     */
    private Integer isSettings;
    /**
     * 当前显示报表月份
     */
    private String dataMonth;
}
