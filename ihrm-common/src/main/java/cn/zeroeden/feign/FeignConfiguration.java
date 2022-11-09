package cn.zeroeden.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author: Zero
 * @time: 2022/11/9
 * @description: Feign的配置--主要是配置拦截器
 */

@Configuration
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取请求属性
                ServletRequestAttributes attributes = (ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes();
                if (attributes != null) {
                    // 获取请求
                    HttpServletRequest request = attributes.getRequest();
                    // 获取原有请求的所有请求头信息
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            String name = headerNames.nextElement();
                            String values = request.getHeader(name);
                            // 把原有请求头信息装载进Feign远程调用时的请求
                            template.header(name, values);
                        }
                    }
                }
            }
        };
    }
}
