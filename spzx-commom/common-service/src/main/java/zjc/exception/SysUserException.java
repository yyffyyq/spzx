package zjc.exception;

import lombok.Data;
import zjc.vo.common.ResultCodeEnum;
@Data
public class SysUserException extends RuntimeException{
    public Integer code;
    public String message;

    public ResultCodeEnum resultCodeEnum;

    public SysUserException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
