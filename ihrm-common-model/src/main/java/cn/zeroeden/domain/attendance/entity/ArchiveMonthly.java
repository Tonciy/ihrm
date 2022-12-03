package cn.zeroeden.domain.attendance.entity;


import cn.zeroeden.domain.attendance.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@TableName("atte_archive_monthly")
@EqualsAndHashCode(callSuper = true)
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ArchiveMonthly extends BaseEntity implements Serializable {


    private String id;
    private String companyId;
    private String departmentId;

    private String archiveYear;
    private String archiveMonth;
    private Integer totalPeopleNum;

    private Integer fullAttePeopleNum;
    private Integer isArchived;
}
