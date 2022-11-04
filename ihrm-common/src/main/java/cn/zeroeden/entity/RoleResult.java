package cn.zeroeden.entity;

import cn.zeroeden.domain.system.Permission;
import cn.zeroeden.domain.system.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResult extends Role {
    private List<String> permIds = new ArrayList<>();
    public RoleResult(Role role){
        BeanUtils.copyProperties(role, this);
        for (Permission permission : role.getPermissions()) {
            this.permIds.add(permission.getId());
        }
    }
}
