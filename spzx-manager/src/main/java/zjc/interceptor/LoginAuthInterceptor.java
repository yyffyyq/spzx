package zjc.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import zjc.entity.system.SysUser;
import zjc.utils.AuthContextUtil;
import zjc.vo.common.Result;
import zjc.vo.common.ResultCodeEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求方式
        //对请求方式做判断 （options ,预检请求，直接放行）
        String method = request.getMethod();
        if("OPTIONS".equals(method)){
            return true;
        }

        //从请求头中获取token
        String token = request.getHeader("token");

        // 如果token为空，返回错误提示
        if(StrUtil.isEmpty(token)){
            responseNoLoginInfo(response);
            return false;
        }
        //拿着token去redis找信息
        String userInfoString = redisTemplate.opsForValue().get("user:login"+token);
        //redis查不到这个信息返回错误提示
        if(StrUtil.isEmpty(userInfoString)){
            responseNoLoginInfo(response);
            return false;
        }
        //如果查询到了，将用户信息存到threadlocal里面
        SysUser sysUser = JSON.parseObject(userInfoString,SysUser.class);
        AuthContextUtil.set(sysUser);
        //把redis用户信息数据过期时间更新一下
        redisTemplate.expire("user:login"+token,30, TimeUnit.MINUTES);
        //放行
        return true;
    }
    private void responseNoLoginInfo(HttpServletResponse response) {
        //TODO这个Result好像有问题原来是Result<Object>
        Result result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //ThreadLocal信息删除
        AuthContextUtil.remove();
    }
}
