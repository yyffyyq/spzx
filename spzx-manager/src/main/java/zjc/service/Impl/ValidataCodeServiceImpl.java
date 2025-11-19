package zjc.service.Impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zjc.service.ValidataCodeService;
import zjc.vo.system.ValidateCodeVo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidataCodeServiceImpl implements ValidataCodeService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //生成图片验证码
    @Override
    public ValidateCodeVo generateValidataCode() {
        //通过工具生成图片验证码
        //hutool
        //int width, int height, int codeCount, int circleCount
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150,48,4,2);
        String codeValue =circleCaptcha.getCode();//4位验证码的值
        String imagesBase64 = circleCaptcha.getImageBase64();  //返回图片验证码，base64编码


        //存储验证到redis，设置key和value
        //设置验证码的过期时间
        String key = UUID.randomUUID().toString().replaceAll("-","");;
        redisTemplate.opsForValue().set("user:validate"+key,
                                        codeValue,
                                        1,
                                        TimeUnit.MINUTES);
        //返回ValidateCodeVo对象
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);//redis存储数据的key
        validateCodeVo.setCodeValue("data:/image/png;base64,"+imagesBase64);//图片验证码显示
        return validateCodeVo;
    }
}
