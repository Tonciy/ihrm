package cn.zeroeden.employee.service.impl;

import cn.zeroeden.domain.employee.EmployeeArchive;
import cn.zeroeden.employee.dao.ArchiveDao;
import cn.zeroeden.employee.service.ArchiveService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * Author:Zero
 * Description:
 */
@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveDao, EmployeeArchive> implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void mySave(EmployeeArchive archive) {
        archive.setId(idWorker.nextId() + "");
        archive.setCreateTime(new Date());
        archiveDao.insert(archive);
    }

//    protected Specification<T> getSpec(String companyId) {
//        Specification<T> spect = new Specification() {
//            @Override
//            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
//                //根据企业id查询
//                return cb.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
//        return spect;
//    }

    @Override
    public EmployeeArchive findLast(String companyId, String month) {
        EmployeeArchive archive = archiveDao.findByLast(companyId, month);
        return archive;
    }
    @Override
    public List<EmployeeArchive> findAll(Integer page, Integer pagesize, String year, String companyId) {
        int index = (page - 1) * pagesize;
        return archiveDao.findAllData(companyId, year + "%", index, pagesize);
    }
    @Override
    public Long countAll(String year, String companyId) {
        return archiveDao.countAllData(companyId, year + "%");
    }

    @Override
    public Page<EmployeeArchive> findSearch(Map<String, Object> map, int page, int size) {
        Page<EmployeeArchive> ipage = new Page<>(page, size);
        LambdaQueryWrapper<EmployeeArchive> queryWrapper = new LambdaQueryWrapper<>();
        if (map.get("companyId") != null && !"".equals(map.get("companyId"))) {
            queryWrapper.eq(EmployeeArchive::getCompanyId, map.get("companyId"));
        }
        if (map.get("year") != null && !"".equals(map.get("year"))) {
            queryWrapper.eq(EmployeeArchive::getMonth, map.get("year"));
        }
        ipage = this.page(ipage, queryWrapper);
        return ipage;
    }

//    /**
//     * 动态条件构建
//     *
//     * @param searchMap
//     * @return
//     */
//    private Specification<EmployeeArchive> createSpecification(Map searchMap) {
//        return new Specification<EmployeeArchive>() {
//            @Override
//            public Predicate toPredicate(Root<EmployeeArchive> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> predicateList = new ArrayList<Predicate>();
//                // 企业id
//                if (searchMap.get("companyId") != null && !"".equals(searchMap.get("companyId"))) {
//                    predicateList.add(cb.like(root.get("companyId").as(String.class), (String) searchMap.get("companyId")));
//                }
//                if (searchMap.get("year") != null && !"".equals(searchMap.get("year"))) {
//                    predicateList.add(cb.like(root.get("mouth").as(String.class), (String) searchMap.get("year")));
//                }
//                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
//            }
//        };
//    }
}
