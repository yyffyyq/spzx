package zjc.mapper;

import org.apache.ibatis.annotations.Mapper;
import zjc.entity.system.SysUser;

@Mapper
public interface SysUserMapper {
    /**
     * 查看数据库内容根据用户名
     * @param userName
     * @return
     */
    SysUser selectUserInfoByUserName(String userName);
}
