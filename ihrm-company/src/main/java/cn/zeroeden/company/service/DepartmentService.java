package cn.zeroeden.company.service;

import cn.zeroeden.domain.company.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    /**
     * 保存部门
     * @param department
     */
    public void add(Department department);

    /**
     * 更新部门
     * @param department
     */
    public void update(Department department);

    /**
     * 根据id查询部门
     * @param id
     */
    public Department findById(String id);

    /**
     * 查询所有部门信息
     */
    public List<Department> findAllByCompanyId(String id);

    /**
     * 根据id删除部门
     * @param id
     */
    public void  deleteById(String id);
}
