package cn.zeroeden.handle;

import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.exception.CommonException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static cn.zeroeden.entity.ResultCode.UNAUTHENTICATED;

/**
 * @author Zero
 * @Description 统一异常处理
 */
@RestControllerAdvice
public class BaseExceptionHandler {


    /**
     * 未登录
     * @return
     */
    @ExceptionHandler(value = CommonException.class)
    public Result commonException(){
        return new Result(UNAUTHENTICATED);
    }


    /**
     * 权限不足
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public Result authorizationException(){
        return new Result(ResultCode.UNAUTHORISE);
    }
    @ExceptionHandler(value = Exception.class)
    public Result error(){
        return new Result(ResultCode.SERVER_ERROR);
    }
}
