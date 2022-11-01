package cn.zeroeden.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

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
}
