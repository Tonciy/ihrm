package cn.zeroeden.system;

import cn.zeroeden.utils.IdWorker;
import cn.zeroeden.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author: Zero
 * @time: 2022/11/1
 * @description:
 */
@SpringBootApplication(scanBasePackages = "cn.zeroeden")
@EntityScan(value = "cn.zeroeden.domain.system")
@EnableEurekaClient  // Eureka服务注册
@EnableDiscoveryClient // 允许发现远程被调用的客户端
@EnableFeignClients  // 运行远程调用接口
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

    /**
     * JWT 工具类
     * @return
     */
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
