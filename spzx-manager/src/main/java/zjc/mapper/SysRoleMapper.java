package zjc.mapper;

import org.apache.ibatis.annotations.Mapper;
import zjc.dto.system.SysRoleDto;
import zjc.entity.system.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> findByPage(SysRoleDto sysRoleDto);
}
