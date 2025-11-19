package zjc.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import zjc.dto.system.LoginDto;
import zjc.entity.system.SysUser;
import zjc.service.SysUserService;
import zjc.service.ValidataCodeService;
import zjc.utils.AuthContextUtil;
import zjc.vo.common.Result;
import zjc.vo.common.ResultCodeEnum;
import zjc.vo.system.LoginVo;
import zjc.vo.system.ValidateCodeVo;

@Tag(name="用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidataCodeService validataCodeService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    //生成图片的验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){
        ValidateCodeVo validateCodeVo = validataCodeService.generateValidataCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }


    //用户登录
    @Operation(summary = "登录的方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    //获取用户信息（优化）
    @GetMapping(value = "/generateInfo")
    public Result<SysUser> getUserInfo(){
        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS);
    }

    //获取用户信息
//    @GetMapping(value = "/generateInfo")
//    public Result<SysUser> getUserInfo(@RequestHeader(name = "token") String token){
//        System.out.println("token:"+token);
//        //获取token
//
//        //根据token查询redis获取用户信息
//        SysUser sysUser = sysUserService.getUserInfo(token);
//        //将用户信息进行返回
//        return Result.build(sysUser, ResultCodeEnum.SUCCESS);
//    }

    //用户退出
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name = "token") String token){
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


}
