package cn.zeroeden.domain.attendance.bo;

import cn.zeroeden.domain.attendance.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;


@Data
public class ArchiveListBO extends BaseEntity implements Serializable {


    private String id;


    /**
     * 总人数
     */
    private String totalPeople;

    /**
     * 全勤认数
     */
    private String fullAttendancePeople;


    /**
     * 月份
     */
    private String month;


}
