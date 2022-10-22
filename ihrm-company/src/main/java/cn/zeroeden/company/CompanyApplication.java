package cn.zeroeden.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @program: ihrm-parent
 * @author: Zero
 * @create: 2022-10-14 16:47
 **/

@SpringBootApplication
@EntityScan(value = "cn.zeroeden.domain.company")
public class CompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
    }
}
