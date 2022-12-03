package cn.zeroeden.domain.attendance.entity;

import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 调休配置表
 */
@EqualsAndHashCode(callSuper = true)

@TableName("atte_day_off_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayOffConfig extends BaseEntity implements Serializable {

    private String id;
    private String companyId;

    /**
     * 部门ID
     */
    private String departmentId;


    /**
     * 调休最后有效日期
     */
    private String latestEffectDate;
    /**
     * 调休单位
     */
    private String unit;


}
