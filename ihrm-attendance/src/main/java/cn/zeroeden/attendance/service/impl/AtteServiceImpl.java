package cn.zeroeden.attendance.service.impl;

import cn.zeroeden.attendance.dao.AttendanceDao;
import cn.zeroeden.attendance.service.AtteService;
import cn.zeroeden.domain.attendance.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AtteServiceImpl extends ServiceImpl<AttendanceDao, Attendance> implements AtteService {


}
