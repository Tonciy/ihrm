package cn.zeroeden.interceptor;

import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.exception.CommonException;
import cn.zeroeden.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
                request.setAttribute("user_claims", claims);
                return true;
            }
        }
        // 未登录/token格式不对
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }
}
