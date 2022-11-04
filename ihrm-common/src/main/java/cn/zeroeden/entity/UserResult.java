package cn.zeroeden.entity;


import cn.zeroeden.domain.system.Role;
import cn.zeroeden.domain.system.User;
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
public class UserResult extends User {
    private List<String> roleIds = new ArrayList<>();

    public UserResult(User user){
        BeanUtils.copyProperties(user, this);
        for (Role role : user.getRoles()) {
            this.roleIds.add(role.getId());
        }
    }
}
