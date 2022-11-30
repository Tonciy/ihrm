package cn.zeroeden.social;

import cn.zeroeden.entity.PageResult;
import cn.zeroeden.social.service.UserSocialService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void test(){
        PageResult all = userSocialService.findAll(1, 10, "1");
        System.out.println(all);
    }
}
