package cn.zeroeden.employee.service;

import cn.zeroeden.domain.employee.EmployeeArchive;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ArchiveService extends IService<EmployeeArchive> {


    public void mySave(EmployeeArchive archive) ;

    public EmployeeArchive findLast(String companyId, String month);

    public List<EmployeeArchive> findAll(Integer page, Integer pagesize, String year, String companyId) ;

    public Long countAll(String year, String companyId);



    public Page<EmployeeArchive> findSearch(Map<String,Object> map, int page, int size);

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
//    public Specification<EmployeeArchive> createSpecification(Map searchMap);
}
