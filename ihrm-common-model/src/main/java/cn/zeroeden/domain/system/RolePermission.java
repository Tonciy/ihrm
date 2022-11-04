package cn.zeroeden.domain.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description: 为角色-权限中间表提供实体
 */
@TableName("pe_role_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {
    private String roleId;
    private String permissionId;
}
