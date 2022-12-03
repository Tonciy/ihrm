package cn.zeroeden.domain.attendance.entity;

import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 加班规则
 * @author itcast
 */

@TableName("atte_extra_duty_rule")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraDutyRule extends BaseEntity implements Serializable {

  private String id;
  private String extraDutyConfigId;
  private String companyId;
  private String departmentId;


  /**
   * 规则内容
   */
  private String rule;
  private String  ruleStartTime;
  private String ruleEndTime;


  /**
   * 是否调休0不调休1调休
   */
  private Integer isTimeOff;

  /**
   * 是否可用 0开启 1 关闭
   */
  private Integer isEnable;


}
