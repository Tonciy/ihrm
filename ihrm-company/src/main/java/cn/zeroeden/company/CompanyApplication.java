package cn.zeroeden.company;

import cn.zeroeden.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * @program: ihrm-parent
 * @author: Zero
 * @create: 2022-10-14 16:47
 **/

@SpringBootApplication(scanBasePackages = "cn.zeroeden")
@EntityScan(value = "cn.zeroeden.domain.company")
public class CompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
    }

    /**
     * UUID-雪花算法
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
