package cn.zeroeden.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (DepartmentApprover)实体类
 */

@TableName("proc_user_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcUserGroup {
    private static final long serialVersionUID = -9084332495284489553L;
    private String id;
    private String name;
    private String param;
    private String isql;
}
