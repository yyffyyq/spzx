package zjc.service;

import zjc.vo.system.ValidateCodeVo;

public interface ValidataCodeService {
    /**
     * 生成图片验证码
     * @return
     */
    ValidateCodeVo generateValidataCode();
}
