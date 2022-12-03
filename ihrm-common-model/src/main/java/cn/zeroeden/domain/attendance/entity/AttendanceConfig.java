package cn.zeroeden.domain.attendance.entity;


import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@TableName("atte_attendance_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 *考勤配置表
 */
public class AttendanceConfig extends BaseEntity implements Serializable {

    private String id;

    private String companyId;

    @NotBlank(message = "部门ID不能为空")
    private String departmentId;

    @NotNull(message = "上午上班时间不能为空")
    private String morningStartTime;
    @NotNull(message = "上午下班时间不能为空")
    private String morningEndTime;
    @NotNull(message = "下午上班时间不能为空")
    private String afternoonStartTime;
    @NotNull(message = "下午下班时间不能为空")
    private String afternoonEndTime;


}
