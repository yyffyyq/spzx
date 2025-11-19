package zjc.service;

import zjc.dto.system.LoginDto;
import zjc.entity.system.SysUser;
import zjc.vo.system.LoginVo;

public interface SysUserService {
    /**
     * 登录功能
     * @param loginDto
     * @return
     */
    LoginVo login(LoginDto loginDto);

    /**
     * 获取用户信息通过token
     * @param token
     * @return
     */
    SysUser getUserInfo(String token);

    /**
     *用户退出方法
     * @param token
     */
    void logout(String token);
}
