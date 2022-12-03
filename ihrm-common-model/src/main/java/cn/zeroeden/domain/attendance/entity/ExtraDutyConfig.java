package cn.zeroeden.domain.attendance.entity;

import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 加班配置表
 */

@TableName("atte_extra_duty_config")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraDutyConfig extends BaseEntity implements Serializable {

  private String id;
  private String companyId;
  private String departmentId;

  /**
   * 每日标准工作时长，单位小时
   */
  private String workHoursDay;
  /**
   * 是否打卡0开启1关闭
   */
  private Integer isClock;
  /**
   * 是否开启加班补偿0开启1关闭
   */
  private String isCompensationint;


}
