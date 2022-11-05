package cn.zeroeden.handle;

import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.exception.CommonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static cn.zeroeden.entity.ResultCode.UNAUTHENTICATED;

/**
 * @author Zero
 * @Description 统一异常处理
 */
@RestControllerAdvice
public class BaseExceptionHandler {


    @ExceptionHandler(value = CommonException.class)
    public Result commonException(){
        return new Result(UNAUTHENTICATED);
    }
    @ExceptionHandler(value = Exception.class)
    public Result error(){
        return new Result(ResultCode.SERVER_ERROR);
    }
}
