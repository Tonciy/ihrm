package cn.zeroeden.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * @author: Zero
 * @time: 2022/11/26
 * @description:
 */

@Component
public class LoginFilter extends ZuulFilter {
    /**
     * 拦截位置
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 拦截优先级，越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * 判断请求是否应该拦截
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 拦截的具体业务逻辑处理
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("zuul 拦截了");
        return null;
    }
}
