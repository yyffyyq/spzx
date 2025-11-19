package zjc.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import zjc.dto.system.LoginDto;
import zjc.entity.system.SysUser;
import zjc.exception.SysUserException;
import zjc.mapper.SysUserMapper;
import zjc.service.SysUserService;
import zjc.vo.common.ResultCodeEnum;
import zjc.vo.system.LoginVo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //图片验证码验证部分
        //获取输入的验证码和存储到redis的key名称
        String captcha = loginDto.getCaptcha();
        String key = loginDto.getCodeKey();

        //根据获取到的redis里的key，查询redis里的存储验证码
        //set("user:validate"+key,
        String redisCode = redisTemplate.opsForValue().get("user:validate"+key);

        //比较输入验证码和redis里的是否一致
        //如果不一致，提示用户验证码校验失败
        if(StrUtil.isEmpty(redisCode)||!StrUtil.equalsIgnoreCase(redisCode,captcha)){
            throw new SysUserException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //如果一直，删除redis里的验证码
        redisTemplate.delete("user:validate"+key);

        //用户账号密码验证部分

        //获取提交了的loginDto
        String userName = loginDto.getUserName();
        //查询数据库对比密码
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);
        //查询失败，抛出异常
        if(sysUser==null){
//            throw new RuntimeException("用户名不存在");
            throw new SysUserException(ResultCodeEnum.LOGIN_ERROR);
        }
        //查询成功，返回成功值
        String database_password = sysUser.getPassword();
        //对输入密码加密，使用m5
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        //比较
        //获取输入的密码，比较数据密码和数据库中密码是否一致
        //判断是否正确，错误就抛出密码错误异常
        if(!input_password.equals(database_password)){
//            throw new RuntimeException("登录失败，密码错误");
            throw new SysUserException(ResultCodeEnum.LOGIN_ERROR);
        }
        //生成一个用户的唯一标识
        String token = UUID.randomUUID().toString().replaceAll("-","");

        //把登录成功用户信息放到redis里面
        //key : token  value:用户信息
        JSON.toJSONString(sysUser);
        redisTemplate.opsForValue().set("user:login"+token,
                                        JSON.toJSONString(sysUser),  /// 将对象转成字符串
                                        7,
                                        TimeUnit.DAYS);

        //返回loginvo对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {

        String userJson = redisTemplate.opsForValue().get("user:login"+token);
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);  ///将字符串转成对象
        System.out.println(sysUser);
        return sysUser;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login"+token);
    }
}
