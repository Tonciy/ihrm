package cn.zeroeden.system;

import cn.zeroeden.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * @author: Zero
 * @time: 2022/11/1
 * @description:
 */
@SpringBootApplication(scanBasePackages = "cn.zeroeden")
@EntityScan(value = "cn.zeroeden.domain.system")
public class SystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(SystemApplication.class, args);
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
