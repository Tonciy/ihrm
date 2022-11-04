package cn.zeroeden.domain.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description: 为用户—角色中间表提供实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pe_user_role")
public class UserRole {
    private String roleId;
    private String userId;
}
