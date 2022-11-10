package cn.zeroeden.employee.dao;

import cn.zeroeden.domain.employee.EmployeeArchive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据访问接口
 */
@Mapper
public interface ArchiveDao extends BaseMapper<EmployeeArchive> {
    @Select(value = "SELECT * FROM em_archive WHERE company_id = #{companyId} AND month = #{month} ORDER BY create_time DESC LIMIT 1;")
    EmployeeArchive findByLast(String companyId, String month);

    @Select(value = "SELECT * FROM em_archive WHERE company_id = #{companyId} AND month LIKE #{year} GROUP BY month HAVING MAX(create_time) limit #{index}, #{pagesize} ")
    List<EmployeeArchive> findAllData(String companyId, String year, Integer index, Integer pagesize);

    @Select(value = "SELECT count(DISTINCT month) FROM em_archive WHERE company_id = #{companyId} AND month LIKE #{year}")
    long countAllData(String companyId, String year);
}