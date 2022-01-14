package fun.whmy.adminfast.admin.controller.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.controller.AbstractBaseController;
import fun.whmy.adminfast.admin.model.basic.CommonPage;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.constant.AdminRole;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleResourceRelationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@AllArgsConstructor
@RestController
@RequestMapping("/rest/resource")
public class UserResourceController extends AbstractBaseController {
    private TbUserResourceService resourceService;
    private TbUserRoleResourceRelationService userRoleResourceRelationService;

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_RESOURCE)
    @PostMapping("/update")
    public ResultBean update(@RequestBody TbUserResourceBean userResourceBean) {
        boolean success = resourceService.updateUnion(userResourceBean);
        if (success) {
            return success();
        } else {
            return fail(ErrorCode.ERROR, "添加失败");
        }
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_RESOURCE)
    @GetMapping("/{id}")
    public ResultBean getItem(@PathVariable Long id) {
        ResultBean ret = new ResultBean();
        TbUserResourceBean umsResource = resourceService.getById(id);
        ret.addEntry("resource", umsResource);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_RESOURCE)
    @PostMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        boolean success = resourceService.removeById(id);
        if (success) {
            return success();
        } else {
            return fail(ErrorCode.ERROR, "添加失败");
        }
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_RESOURCE)
    @GetMapping("/list")
    public ResultBean list(@RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String nameKeyword,
                           @RequestParam(required = false) String codeKeyword,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        ResultBean ret = new ResultBean();
        Page<TbUserResourceBean> resourceList = resourceService.list(categoryId, nameKeyword, codeKeyword, pageSize, pageNum);
        CommonPage<TbUserResourceBean> page = CommonPage.restPage(resourceList);
        ret.addEntry("page", page);
        return ret;
    }

    @SaCheckPermission()
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public ResultBean listAll() {
        final Long companyId = getCurrentCompanyId();
        final Long userId = getCurrentUserId();
        ResultBean ret = new ResultBean();
        QueryWrapper<TbUserResourceBean> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserResourceBean> lambda = queryWrapper.lambda();

        lambda.eq(TbUserResourceBean::getIsdelete, 0);
        List<TbUserResourceBean> resourceList = resourceService.list(queryWrapper);


        //获取当前用户资源 非系统管理员 查询自己拥有的资源信息并排除不存在的resourceId
        if (companyId != 0) {
            //获取次用户的所有资源信息
            List<String> resourceCodeList = resourceService.getUserResourceCodesById(userId);
            List<TbUserResourceBean> resultList = resourceList.stream().filter(a ->
                    resourceCodeList.contains(a.getCode())
            ).collect(Collectors.toList());
            ret.addEntry("list", resultList);
        } else {
            ret.addEntry("list", resourceList);
        }
        return ret;
    }
}

