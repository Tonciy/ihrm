package cn.zeroeden.exception;

import cn.zeroeden.entity.ResultCode;

/**
 * @author: Zero
 * @time: 2022/11/5
 * @description: 通用异常
 */

public class CommonException extends Exception{

    public CommonException(String context){
        super(context);
    }
    public CommonException(ResultCode context){
        super(context.message());
    }
}
