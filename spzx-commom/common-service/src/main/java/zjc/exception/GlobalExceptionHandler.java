package zjc.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zjc.vo.common.Result;
import zjc.vo.common.ResultCodeEnum;

@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    //自定义异常抛出
    @ExceptionHandler(SysUserException.class)
    @ResponseBody
    public Result SysUsererror(SysUserException e){
        return Result.build(null, e.resultCodeEnum);
    }
}
