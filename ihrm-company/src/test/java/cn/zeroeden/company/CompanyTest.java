package cn.zeroeden.company;

import cn.zeroeden.company.dao.CompanyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * @author Zero
 * @Description 描述此类
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyTest {
    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test(){
//        List<Company> all = companyDao.selectList(null);
//        for (Company company : all) {
//            System.out.println(all);
//        }
        HashMap<String, String> map1 = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        map1.put("1","1");
        map2.put("1","2");
        map1.putAll(map2);
        System.out.println(map1);
    }
}
