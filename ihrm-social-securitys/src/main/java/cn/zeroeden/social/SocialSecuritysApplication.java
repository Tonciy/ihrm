package cn.zeroeden.social;

import cn.zeroeden.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author: Zero
 * @time: 2022/11/27
 * @description:
 */

@SpringBootApplication(scanBasePackages = "cn.zeroeden")
//2.配置jpa注解的扫描
@EntityScan(value="cn.zeroeden.domain")
//3.注册到eureka
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SocialSecuritysApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialSecuritysApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}
