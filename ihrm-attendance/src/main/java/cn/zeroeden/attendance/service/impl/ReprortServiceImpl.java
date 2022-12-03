package cn.zeroeden.attendance.service.impl;


import cn.zeroeden.attendance.dao.ArchiveMonthlyDao;
import cn.zeroeden.attendance.service.ReprortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReprortServiceImpl implements ReprortService {




    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;

}
