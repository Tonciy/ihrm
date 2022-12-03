package cn.zeroeden.domain.attendance.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 考勤统计结果
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtteStatisBO implements Serializable {

    private String id;


    private String day;


    private Integer adtStatu;

}
