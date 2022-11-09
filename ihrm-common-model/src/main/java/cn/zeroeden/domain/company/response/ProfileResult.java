package cn.zeroeden.domain.company.response;

import cn.zeroeden.domain.system.Permission;
import cn.zeroeden.domain.system.Role;
import cn.zeroeden.domain.system.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Zero
 * @time: 2022/11/5
 * @description: 用户登录后后去其前端路由权限信息
 */

@Data
@NoArgsConstructor
public class ProfileResult implements Serializable, AuthCachePrincipal {
    private String userId;
    private String mobile;
    private String username;
    private String company;
    private String companyId;
    private Map<String, Object> roles = new HashMap<>();

    public ProfileResult(User user, List<Permission> permissions){
        this.mobile = user.getMobile();
        this.username  = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Permission permission : permissions) {
            String code = permission.getCode();
            if(permission.getType() == 1){
                menus.add(code);
            }else if(permission.getType() == 2){
                points.add(code);
            }else {
                apis.add(code);
            }
        }this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);

    }
    public ProfileResult(User user){
        this.mobile = user.getMobile();
        this.username  = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        Set<Role> role2 = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : role2) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                String code = permission.getCode();
                if(permission.getType() == 1){
                    menus.add(code);
                }else if(permission.getType() == 2){
                    points.add(code);
                }else {
                    apis.add(code);
                }
            }
        }
        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
