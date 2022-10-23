package cn.zeroeden.company;

import cn.zeroeden.company.dao.CompanyDao;
import cn.zeroeden.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
        List<Company> all = companyDao.selectList(null);
        for (Company company : all) {
            System.out.println(all);
        }
    }
}
