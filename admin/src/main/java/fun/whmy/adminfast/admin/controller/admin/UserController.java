package fun.whmy.adminfast.admin.controller.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.controller.AbstractBaseController;
import fun.whmy.adminfast.admin.model.basic.CommonPage;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBeanExtend;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import fun.whmy.adminfast.admin.model.constant.AdminRole;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleService;
import fun.whmy.adminfast.admin.service.TbUserService;
import fun.whmy.adminfast.admin.utils.RSAUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/rest/user")
public class UserController extends AbstractBaseController {

    private TbUserService userService;
    private TbUserRoleService roleService;
    private TbUserResourceService resourceService;

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER)
    @GetMapping("/page")
    public ResultBean readUserPage(@RequestParam("keyword") String keyword,
                                   @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNum") Integer pageNum) {
        ResultBean ret = new ResultBean();
        Long companyId = StpUtil.getSession().getLong("companyId");
        final Page<TbUserBeanExtend> page = userService.listExtend(companyId, keyword, pageSize, pageNum);
        ret.addEntry("page", CommonPage.restPage(page));
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER_EDIT)
    @PostMapping("/add/{module}")
    public ResultBean addSystemUser(@RequestBody TbUserBean userBean, @PathVariable String module) {
        ResultBean ret = new ResultBean();
        final Long userId = getCurrentUserId();
        Long companyId = StpUtil.getSession().getLong("companyId");
        if ((userBean == null) || (StrUtil.isEmpty(userBean.getUserName()))
                || (StrUtil.isEmpty(userBean.getPassword()))) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("参数错误");
            log.error("addSystemUser, the parameter is error.");
            return ret;
        }

//        if (companyId == 0 && userBean.getCompanyId() == null) {
//            ret.setCode(ErrorCode.ERROR);
//            ret.setMsg("参数错误,需填写公司信息");
//            log.error("addSystemUser, the parameter is error.");
//            return ret;
//        }
        if (companyId != 0) {
            userBean.setCompanyId(companyId);
        }
        String userName = userBean.getUserName();
        String password = RSAUtils.decryptStringByJs(userBean.getPassword(), module);
        if (StrUtil.isEmpty(password)) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("参数错误");
            log.error("addSystemUser, the password and confrim password is not equal.");
            return ret;
        }
        userBean.setPassword(password);
        TbUserBean checkUser = userService.getUserByName(userName);
        if (checkUser != null) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("用户已存在");
            log.error("addSystemUser, the system user={} is exist.", userName);
            return ret;
        }


        userBean.setCreateId(userId);
        userBean.setUpdateId(userId);

        userService.addUser(userBean);
        log.info("addAdminUser success, user:" + userName);

        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER_EDIT)
    @PostMapping("/edit/{module}")
    public ResultBean editSystemUser(@RequestBody TbUserBean systemUserBean, @PathVariable String module) {
        ResultBean ret = new ResultBean();
        final Long userId = getCurrentUserId();
        Long companyId = StpUtil.getSession().getLong("companyId");
        if ((systemUserBean == null) || StrUtil.isEmpty(systemUserBean.getUserName())) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("参数错误");
            log.error("addSystemUser, the parameter is error.");
            return ret;
        }
//        if (companyId == 0 && systemUserBean.getCompanyId() == null) {
//            ret.setCode(ErrorCode.ERROR);
//            ret.setMsg("参数错误,需填写公司信息");
//            log.error("addSystemUser, the parameter is error.");
//            return ret;
//        }

        if (companyId != 0) {
            systemUserBean.setCompanyId(companyId);
        }

        String userName = systemUserBean.getUserName();
        TbUserBean checkUser = userService.getById(systemUserBean.getUserId());
        if (checkUser == null) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("用户不存在");
            log.error("editSystemUser, the system user={} is not exist.", userName);
            return ret;
        }

        if (companyId != 0 && checkUser.getCompanyId() != companyId) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("不能修改其他公司的用户信息");
            log.error("editSystemUser, the parameter is error.");
            return ret;
        }


        if (StrUtil.isNotEmpty(systemUserBean.getPassword())) {
            String password = RSAUtils.decryptStringByJs(systemUserBean.getPassword(), module);
            systemUserBean.setPassword(password);
        }

        systemUserBean.setUpdateId(userId);

        userService.editUser(systemUserBean);
        log.info("editSystemUser success, user:" + userName);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER_EDIT)
    @PostMapping("/del")
    public ResultBean deleteUser(@RequestParam("id") Long id) {
        ResultBean ret = new ResultBean();
        TbUserBean checkUser = userService.getById(id);
        final Long userId = getCurrentUserId();
        Long companyId = StpUtil.getSession().getLong("companyId");
        if (checkUser == null) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("用户不存在");
            log.error("editSystemUser, the system user={} is not exist.", id);
            return ret;
        }
        if (companyId != 0 && checkUser.getCompanyId() != companyId) {
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("不能修改其他公司的用户信息");
            log.error("addSystemUser, the parameter is error.");
            return ret;
        }
        userService.deleteById(id, userId);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER_EDIT)
    @PostMapping("/role/update")
    public ResultBean updateRole(@RequestParam("userId") Long userId,
                                 @RequestParam("roleIds") List<Long> roleIds) {

        int count = roleService.updateUserRole(userId, roleIds);
        if (count >= 0) {
            return new ResultBean();
        }
        return new ResultBean(ErrorCode.ERROR, "操作失败");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER)
    @RequestMapping(value = "/role/{userId}", method = RequestMethod.GET)
    public ResultBean getRoleList(@PathVariable Long userId) {
        ResultBean ret = new ResultBean();
        List<TbUserRoleBean> roleList = roleService.getRoleList(userId);
        ret.addEntry("roleList", roleList);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_USER)
    @RequestMapping(value = "/roleListAllCanAllo", method = RequestMethod.GET)
    public ResultBean listAll() {
        ResultBean ret = new ResultBean();
        final Long companyId = getCurrentCompanyId();
        List<TbUserRoleBean> roleList = roleService.list();
        // 系统管理员
        ret.addEntry("roleList", roleList);
        for (TbUserRoleBean roleBean : roleList) {
            roleBean.setSort(companyId.intValue() == roleBean.getCompanyId() ? 1 : 0);
        }
        roleList.sort((a, b) -> b.getSort() - a.getSort());
        return ret;
    }


    /**
     * 用户获取个人信息
     */
    @SaCheckPermission()
    @GetMapping("/info")
    public ResultBean getUserInfo() {
        ResultBean ret = new ResultBean();
        final Long userId = getCurrentUserId();
        TbUserBean user = userService.getById(userId);
        ret.addEntry("user", user);
        final List<String> roles = roleService.getUserRoleCodesById(userId);
        List<TbUserResourceBean> resource;
        if (roles.contains("admin")) {
            QueryWrapper<TbUserResourceBean> queryWrapper = new QueryWrapper<>();
            final LambdaQueryWrapper<TbUserResourceBean> lambda = queryWrapper.lambda();
            lambda.eq(TbUserResourceBean::getIsdelete, 0);
            resource = resourceService.list(queryWrapper);
        } else {
            resource = resourceService.getUserResourceById(userId);
        }
        ret.addEntry("role", resource);
        return ret;
    }
}

