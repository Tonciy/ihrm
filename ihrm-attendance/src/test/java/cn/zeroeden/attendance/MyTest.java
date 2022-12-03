package cn.zeroeden.attendance;

import cn.zeroeden.attendance.service.ConfigurationService;
import cn.zeroeden.domain.attendance.entity.AttendanceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Zero
 * @time: 2022/12/3
 * @description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private ConfigurationService configurationService;

    @Test
    public void test(){
        AttendanceConfig attendanceConfig = configurationService.getAttendanceConfig("1", "1066240303092076544");
        System.out.println(attendanceConfig);
    }
}
