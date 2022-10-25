package cn.zeroeden.handle;

import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Zero
 * @Description 统一异常处理
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result error(){
        return new Result(ResultCode.SERVER_ERROR);
    }
}
