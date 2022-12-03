package cn.zeroeden.domain.attendance.entity;


import cn.zeroeden.domain.attendance.base.BaseEntity;
import cn.zeroeden.domain.attendance.vo.AtteUploadVo;
import cn.zeroeden.domain.system.User;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤表
 */
@EqualsAndHashCode(callSuper = true)
@TableName("atte_attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 594829320797158219L;

    private String id;
    private String companyId;
    private String departmentId;

    private String userId;
    private Integer adtStatu;
    private long jobStatu;

    private Date adtInTime;
    private String adtInPlace;
    private String adtInHourse;

    private String adtInCoordinate;
    private Date adtOutTime;
    private String adtOutPlace;
    private String adtOutHourse;
    private String day;

    public Attendance(AtteUploadVo vo, User user) {
        this.adtInTime = vo.getInTime();
        this.adtOutTime = vo.getOutTime();
        this.userId = user.getId();
        this.companyId = user.getCompanyId();
        this.departmentId = user.getDepartmentId();
        this.jobStatu = user.getInServiceStatus();
    }

}
