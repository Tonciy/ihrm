package cn.zeroeden.domain.attendance.base;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseEntity implements Serializable {


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 备注
     */
    private String remarks;
}
