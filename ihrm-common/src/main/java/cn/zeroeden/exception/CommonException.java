package cn.zeroeden.exception;

/**
 * @author: Zero
 * @time: 2022/11/5
 * @description: 通用异常
 */

public class CommonException extends Exception{

    public CommonException(String context){
        super(context);
    }
}
