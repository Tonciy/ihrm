package cn.zeroeden.employee;

import cn.zeroeden.utils.IdWorker;
import cn.zeroeden.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

//@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.zeroeden")
@EntityScan("cn.zeroeden.domain.employee")
@EnableEurekaClient
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public JwtUtils jwtUtil() {
        return new JwtUtils();
    }
}