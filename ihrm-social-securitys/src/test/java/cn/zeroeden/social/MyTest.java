package cn.zeroeden.social;

import cn.zeroeden.domain.socialSecuritys.Archive;
import cn.zeroeden.domain.socialSecuritys.ArchiveDetail;
import cn.zeroeden.entity.PageResult;
import cn.zeroeden.social.service.ArchiveService;
import cn.zeroeden.social.service.UserSocialService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: Zero
 * @time: 2022/11/30
 * @description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private UserSocialService userSocialService;

    @Autowired
    private ArchiveService archiveService;

    @Test
    public void test(){
        PageResult all = userSocialService.findAll(1, 10, "1");
        System.out.println(all);
    }

    @Test
    public void test1(){
        List<Archive> archiveByYearAndCompnayId = archiveService.findArchiveByYearAndCompnayId("1", "2019");
        System.out.println(archiveByYearAndCompnayId);
    }

    @Test
    public void test2(){
        ArchiveDetail data = archiveService.findUserArchiveDetailByUserIdAndYearMonth("1066370498633486336", "201908");
        System.out.println(data);
    }
}
