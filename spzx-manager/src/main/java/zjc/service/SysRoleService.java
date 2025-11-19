package zjc.service;

import com.github.pagehelper.PageInfo;
import zjc.dto.system.SysRoleDto;
import zjc.entity.system.SysRole;

public interface SysRoleService {
    //实现角色列表
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, int limit);
}
