package cn.zeroeden.company.service.impl;

import cn.zeroeden.company.dao.DepartmentDao;
import cn.zeroeden.company.service.DepartmentService;
import cn.zeroeden.domain.company.Department;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentDao, Department> implements DepartmentService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DepartmentDao departmentDao;
    @Override
    public Department findByCode(String code, String companyId) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getCode, code);
        wrapper.eq(Department::getCompanyId, companyId);
        Department department = departmentDao.selectOne(wrapper);
        return department;
    }


    @Override
    public void add(Department department) {
        //填充其他参数
        department.setId(idWorker.nextId() + "");
        department.setCreateTime(new Date());
        departmentDao.insert(department);
    }

    @Override
    public void update(Department department) {
        Department sourceDepartment = departmentDao.selectById(department.getId());
        sourceDepartment.setName(department.getName());
        sourceDepartment.setPid(department.getPid());
        sourceDepartment.setManagerId(department.getManagerId());
        sourceDepartment.setIntroduce(department.getIntroduce());
        sourceDepartment.setManager(department.getManager());
        departmentDao.updateById(sourceDepartment);
    }

    @Override
    public Department findById(String id) {
        return departmentDao.selectById(id);
    }

    @Override
    public List<Department> findAllByCompanyId(String id) {
        LambdaQueryWrapper<Department> tem = new LambdaQueryWrapper<>();
        tem.eq(Department::getCompanyId, id);
        return departmentDao.selectList(tem);
    }

    @Override
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }
}
