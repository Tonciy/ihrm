package cn.zeroeden.salarys.service.impl;


import cn.zeroeden.domain.salarys.SalaryArchive;
import cn.zeroeden.salarys.dao.ArchiveDao;
import cn.zeroeden.salarys.dao.ArchiveDetailDao;
import cn.zeroeden.salarys.service.ArchiveService;
import cn.zeroeden.utils.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveDao, SalaryArchive> implements ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private ArchiveDetailDao archiveDetailDao;
    @Autowired
    private IdWorker idWorker;

}
