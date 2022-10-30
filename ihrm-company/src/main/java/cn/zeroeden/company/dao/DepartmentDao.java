package cn.zeroeden.company.dao;

import cn.zeroeden.domain.company.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentDao  extends BaseMapper<Department> {
}
