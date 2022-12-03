package cn.zeroeden.domain.attendance.entity;


import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 扣款类型表
 */
@EqualsAndHashCode(callSuper = true)
@TableName("atte_deduction_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionType extends BaseEntity implements Serializable {


  private String code;
  private String description;



}
