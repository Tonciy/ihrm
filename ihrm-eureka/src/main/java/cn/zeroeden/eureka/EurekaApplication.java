package cn.zeroeden.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author: Zero
 * @time: 2022/11/9
 * @description:
 */

@SpringBootApplication
@EnableEurekaServer // 开启Eureka服务端配置
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
