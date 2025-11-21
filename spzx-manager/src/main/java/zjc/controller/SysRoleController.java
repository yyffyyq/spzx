package zjc.controller;


import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zjc.dto.system.SysRoleDto;
import zjc.entity.system.SysRole;
import zjc.service.SysRoleService;
import zjc.vo.common.Result;
import zjc.vo.common.ResultCodeEnum;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    //角色列表的方法
    // current：当前页 limit:每页显示记录数
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current") Integer current,
                             @PathVariable("limit") int limit,
                             @RequestBody SysRoleDto sysRoleDto) {
        //pageHeader方法实现分页
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto,current,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加角色
    @PostMapping(value = "/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //修改角色
    @PutMapping(value = "/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

    //删除角色
    @PutMapping(value = "/deleteSysRole")
    public Result deletSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.deleteSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
