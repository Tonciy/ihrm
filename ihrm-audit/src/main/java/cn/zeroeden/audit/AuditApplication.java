package cn.zeroeden.audit;


import cn.zeroeden.utils.IdWorker;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

//1.配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "cn.zeroeden",exclude ={ SecurityAutoConfiguration.class} )
//2.配置Mybatis注解的扫描
@MapperScan(value="cn.zeroeden.domain")
//3.注册到eureka
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class AuditApplication {

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(AuditApplication.class,args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    /**
     * 为了解决 activiti 对象转化json的问题
     *    过滤掉identityLinks属性
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("identityLinks");
        config.setSerializeFilters(filter);
        fjc.setFastJsonConfig(config);
        HttpMessageConverter<?> converter = fjc;
        return new HttpMessageConverters(converter);
    }
}
