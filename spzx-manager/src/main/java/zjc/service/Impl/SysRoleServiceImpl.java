package zjc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zjc.config.CustomIdGenerator;
import zjc.dto.system.SysRoleDto;
import zjc.entity.system.SysRole;
import zjc.exception.SysUserException;
import zjc.mapper.SysRoleMapper;
import zjc.mapper.SysUserMapper;
import zjc.service.SysRoleService;
import zjc.vo.common.ResultCodeEnum;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private CustomIdGenerator customIdGenerator;


    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, int limit) {
        //设置分页参数
        PageHelper.startPage(current, limit);
        //根据条件查询所有数据
        List<SysRole> list = sysRoleMapper.findByPage(sysRoleDto);
        //封装pageInfo对象
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void saveSysRole(SysRole sysRole) {
        //设置Id
        Long customId = customIdGenerator.generateCustomId();
        if(sysRole.getRoleName() == "" || sysRole.getRoleCode() == ""){
            throw new SysUserException(ResultCodeEnum.ACCOUNT_COUNTBENULL);
        }
        sysRole.setId(customId);
        //调用mapper将信息存入数据库
        sysRoleMapper.save(sysRole);
    }

    @Override
    public void updateSysRole(SysRole sysRole) {
        //先找到这个角色

        //将发来的表单内容获取下来用于替换原本的角色信息
        sysRoleMapper.update(sysRole);
        //更新成功返回成功值
    }

    @Override
    public void deleteSysRole(SysRole sysRole) {
        sysRoleMapper.delete(sysRole);

    }
}
