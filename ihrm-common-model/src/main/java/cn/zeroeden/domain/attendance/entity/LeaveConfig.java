package cn.zeroeden.domain.attendance.entity;


import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请假配置表
 */
@EqualsAndHashCode(callSuper = true)
@TableName("atte_leave_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveConfig extends BaseEntity implements Serializable {

  private String id;

  private String companyId;

  private String departmentId;

  private String leaveType; //类型

  private Integer isEnable;


}
