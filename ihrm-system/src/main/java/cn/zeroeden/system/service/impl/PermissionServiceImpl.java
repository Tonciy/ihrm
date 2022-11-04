package cn.zeroeden.system.service.impl;

import cn.zeroeden.domain.system.*;
import cn.zeroeden.system.dao.PermissionApiDao;
import cn.zeroeden.system.dao.PermissionDao;
import cn.zeroeden.system.dao.PermissionMenuDao;
import cn.zeroeden.system.dao.PermissionPointDao;
import cn.zeroeden.system.service.PermissionService;
import cn.zeroeden.utils.BeanMapUtils;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static cn.zeroeden.utils.PermissionConstants.*;

/**
 * @author: Zero
 * @time: 2022/11/4
 * @description:
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;

    @Override
    public void add(Map<String, Object> map) throws Exception {
        String id = idWorker.nextId() + "";
        // 通过工具类将map转化为我们想要的Permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        // 根据不同的权限类型构建不同的资源对象
        int type = permission.getType();
        switch (type) {
            case PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.insert(menu);
                break;
            case PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(id);
                permissionPointDao.insert(point);
                break;
            case PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(id);
                permissionApiDao.insert(api);
                break;
            default:
                throw new Exception();
        }
        permissionDao.insert(permission);
    }

    @Override
    public void update(Map<String, Object> map) throws Exception {
        // 通过工具类将map转化为我们想要的Permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        // 通过权限id查询权限数据，并设置值--防止前端传过来不存在的值设为NULL
        Permission perm = permissionDao.selectById(permission.getId());
        perm.setName(permission.getName());
        perm.setCode(permission.getCode());
        perm.setDescription(perm.getDescription());
        perm.setEnVisible(perm.getEnVisible());
        // 根据类型构造不同资源
        int type = permission.getType();
        switch (type) {
            case PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(perm.getId());
                permissionMenuDao.insert(menu);
                break;
            case PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(perm.getId());
                permissionPointDao.insert(point);
                break;
            case PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(perm.getId());
                permissionApiDao.insert(api);
                break;
            default:
                throw new Exception();
        }
        permissionDao.insert(perm);
    }

    @Override
    public Map<String, Object> findById(String id) throws Exception {
        Permission permission = permissionDao.selectById(id);
        int type = permission.getType();
        Object obj = null;
        switch (type) {
            case PY_MENU:
                obj = permissionMenuDao.selectById(id);
                break;
            case PY_POINT:
                obj = permissionPointDao.selectById(id);
                break;
            case PY_API:
                obj = permissionApiDao.selectById(id);
                break;
            default:
                throw new Exception();
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(obj);
        Map<String, Object> map2 = BeanMapUtils.beanToMap(permission);
        map.putAll(map2);
        return map;
    }

    @Override
    public List<Permission> findAll(Map<String, Object> map) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(map.get("pid"))) {
            queryWrapper.eq(Permission::getPid, map.get("pid"));
        }
        if (!StringUtils.isEmpty(map.get("enVisible"))) {
            queryWrapper.eq(Permission::getEnVisible, map.get("enVisible"));
        }
        // type: 0:菜单 + 按钮          1：菜单   2：按钮   3：API接口
        if (!StringUtils.isEmpty(map.get("type"))) {
            String type = (String) map.get("type");
            if ("0".equals(type)) {
                queryWrapper.inSql(Permission::getType, "1,2");
            } else {
                queryWrapper.eq(Permission::getType, type);
            }
        }

        List<Permission> list = permissionDao.selectList(queryWrapper);

        return list;
    }

    @Override
    public void deleteById(String id) throws Exception{
        Permission permission = permissionDao.selectById(id);
        // 删除权限
        permissionDao.deleteById(id);
        int type = permission.getType();
        switch (type) {
            case PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new Exception();
        }
    }
}
