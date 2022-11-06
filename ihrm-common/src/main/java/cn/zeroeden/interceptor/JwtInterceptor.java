package cn.zeroeden.interceptor;

import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.exception.CommonException;
import cn.zeroeden.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Zero
 * @time: 2022/11/6
 * @description: 登录时认证授权判断
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            Claims claims = jwtUtils.parseJwt(token);
            if(claims != null){
                // 已登录

                // 获取访问API字符串
                String apis = (String)claims.get("apis");
                // 通过注解反射获取每个API接口的唯一标识符
                //  --在这里的是唯一标识符是在Controller的方法上的@RequestMapping的name属性标明的，数据库的API也有
                //  --可以自己自定义注解接口来实现（这样获取时比较容易），使用Restful风格时推荐使用,
                //  -- 使用了Restful风格但是没有统一使用@RequestMapping的话那就根据请求类型来获取注解
                HandlerMethod h = (HandlerMethod) handler;
                //  获取接口上的@RequestMapping注解
                Object annotation = null;
                String method = request.getMethod().toUpperCase();
                String name = null;
                switch (method){
                    case "GET":
                        annotation = h.getMethodAnnotation(GetMapping.class);
                        name = ((GetMapping)annotation).name();
                        break;
                    case "POST":
                        annotation = h.getMethodAnnotation(PostMapping.class);
                        name = ((PostMapping)annotation).name();
                        break;
                    case "DELETE":
                        annotation = h.getMethodAnnotation(DeleteMapping.class);
                        name = ((DeleteMapping)annotation).name();
                        break;
                    case "PUT":
                        annotation = h.getMethodAnnotation(PutMapping.class);
                        name = ((PutMapping)annotation).name();
                        break;
                    default:
                        throw new CommonException("不支持的请求类型");
                }
                if(!StringUtils.isEmpty(apis) && name != null && apis.contains(name)){
                    // 表示具有访问权限
                    request.setAttribute("user_claims", claims);
                    return true;
                }

            }
        }
        // 未登录/token格式不对
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }
}
