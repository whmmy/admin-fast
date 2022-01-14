package fun.whmy.adminfast.admin.controller.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.controller.AbstractBaseController;
import fun.whmy.adminfast.admin.model.basic.CommonPage;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import fun.whmy.adminfast.admin.model.constant.AdminRole;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleService;
import fun.whmy.adminfast.admin.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 前端控制器
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/rest/role")
public class UserRoleController extends AbstractBaseController {

    @Autowired
    private TbUserRoleService roleService;

    @Autowired
    private TbUserResourceService resourceService;

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @PostMapping("/create")
    public ResultBean create(@RequestBody TbUserRoleBean role) {
        if (role == null) {
            return fail("角色不能为空");
        }
        final Long companyId = getCurrentCompanyId();
        final Long userId = getCurrentUserId();
        if (StrUtil.isEmpty(role.getName()) || StrUtil.isEmpty(role.getCode())) {
            return fail("姓名与编码为必填项");
        }
        role.setCompanyId(companyId);

        boolean success = roleService.create(role);
        if (success) {
            return success();
        }
        return fail();
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @PostMapping("/update/{id}")
    public ResultBean update(@PathVariable Long id, @RequestBody TbUserRoleBean role) {
        final Long companyId = getCurrentCompanyId();
        TbUserRoleBean bean = roleService.getById(role.getId());
        if (companyId.longValue() != bean.getCompanyId() && companyId != 0) {
            return fail(ErrorCode.ERROR, "此角色不能被此用户修改");
        }
        role.setId(id);
        boolean success = roleService.updateById(role);
        if (success) {
            return success();
        }
        return fail();
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @PostMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        final Long companyId = getCurrentCompanyId();
        TbUserRoleBean bean = roleService.getById(id);
        if (companyId.longValue() != bean.getCompanyId() && companyId != 0) {
            return fail(ErrorCode.ERROR, "此角色不能被此用户修改");
        }
        boolean success = roleService.removeById(id);
        if (success) {
            return success();
        } else {
            return fail("操作失败");
        }
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER)
    @GetMapping("/listAll")
    public ResultBean listAll() {
        ResultBean ret = new ResultBean();
        final Long companyId = getCurrentCompanyId();
        QueryWrapper<TbUserRoleBean> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserRoleBean> lambda = queryWrapper.lambda();
        lambda.eq(TbUserRoleBean::getCompanyId, companyId);
        List<TbUserRoleBean> roleList = roleService.list(queryWrapper);
        ret.addEntry("role", roleList);
        return ret;
    }

    //暂时不支持管理员查询其他商户的角色信息
    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @GetMapping("/list")
    public ResultBean list(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        ResultBean ret = new ResultBean();
        final Long companyId = getCurrentCompanyId();
        Page<TbUserRoleBean> roleList = roleService.list(keyword, pageSize, pageNum,companyId);
        ret.addEntry("page", CommonPage.restPage(roleList));
        return ret;
    }
    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @PostMapping("/updateStatus/{id}")
    public ResultBean updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        TbUserRoleBean bean = roleService.getById(id);
        final Long companyId = getCurrentCompanyId();
        if (companyId.longValue() != bean.getCompanyId()  && companyId != 0) {
            return fail(ErrorCode.ERROR, "此角色不能被此用户修改");
        }
        TbUserRoleBean umsRole = new TbUserRoleBean();
        umsRole.setId(id);
        umsRole.setStatus(status);
        boolean success = roleService.updateById(umsRole);
        if (success) {
            return new ResultBean();
        } else {
            return new ResultBean(ErrorCode.ERROR, "操作失败");
        }
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @GetMapping("/listResource/{roleId}")
    public ResultBean listResource(@PathVariable Long roleId) {
        ResultBean ret = new ResultBean();
        List<TbUserResourceBean> roleList = resourceService.getResourceListByRoleId(roleId);
        ret.addEntry("resList", roleList);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_ROLE)
    @PostMapping("/allocResource")
    public ResultBean allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        final Long companyId = getCurrentCompanyId();
        TbUserRoleBean bean = roleService.getById(roleId);
        if (companyId.longValue() != bean.getCompanyId() && companyId != 0) {
            return fail(ErrorCode.ERROR, "此角色不能被此用户修改");
        }
        int count = roleService.allocResource(roleId, resourceIds);
        return new ResultBean();
    }

}

