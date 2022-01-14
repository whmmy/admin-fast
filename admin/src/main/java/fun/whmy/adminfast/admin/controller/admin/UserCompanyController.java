package fun.whmy.adminfast.admin.controller.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.controller.AbstractBaseController;
import fun.whmy.adminfast.admin.model.basic.CommonPage;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserCompanyBean;
import fun.whmy.adminfast.admin.model.constant.AdminRole;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import fun.whmy.adminfast.admin.service.TbUserCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/rest/company")
public class UserCompanyController extends AbstractBaseController {

    @Autowired
    private TbUserCompanyService userCompanyService;

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @GetMapping("/list")
    public ResultBean list(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        ResultBean ret = new ResultBean();
        Page<TbUserCompanyBean> list = userCompanyService.list(keyword, pageSize, pageNum);
        CommonPage<TbUserCompanyBean> userCompanyBeanCommonPage = CommonPage.restPage(list);
        ret.addEntry("page", userCompanyBeanCommonPage);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/create")
    public ResultBean create(@RequestBody TbUserCompanyBean userCompanyBean) {
        QueryWrapper<TbUserCompanyBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserCompanyBean> lambda = wrapper.lambda();
        lambda.eq(TbUserCompanyBean::getName, userCompanyBean.getName());
        TbUserCompanyBean checkComapany = userCompanyService.getOne(lambda);
        if (checkComapany != null) {
            return fail("名称有冲突");
        }
        // 生成token
        userCompanyBean.setToken(IdUtil.simpleUUID());
        boolean success = userCompanyService.save(userCompanyBean);
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "提交的内容有冲突");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/update")
    public ResultBean update(@RequestBody TbUserCompanyBean userCompanyBean) {
        //判断名称是否冲突
        QueryWrapper<TbUserCompanyBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserCompanyBean> lambda = wrapper.lambda();
        lambda.eq(TbUserCompanyBean::getName, userCompanyBean.getName());
        lambda.ne(TbUserCompanyBean::getId, userCompanyBean.getId());
        lambda.eq(TbUserCompanyBean::getIsdelete, 0);
        TbUserCompanyBean one = userCompanyService.getOne(lambda);
        if (one != null) {
            return fail("名称有冲突");
        }

        boolean success = userCompanyService.updateById(userCompanyBean);
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "提交的内容有冲突");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/softdel")
    public ResultBean softDel(@RequestParam("id") Long id) {
        boolean success = userCompanyService.softDel(id);
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "删除失败");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER)
    @GetMapping("/all")
    public ResultBean readAll() {
        ResultBean ret = new ResultBean();
        final Long companyId = getCurrentCompanyId();
        List<TbUserCompanyBean> companyAll = new ArrayList<>();
        if (companyId == 0) {
            QueryWrapper<TbUserCompanyBean> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<TbUserCompanyBean> lambda = queryWrapper.lambda();
            lambda.eq(TbUserCompanyBean::getIsdelete, 0);
            companyAll.addAll(userCompanyService.list(queryWrapper));
        } else {
            TbUserCompanyBean bmsCompany = userCompanyService.getById(companyId);
            companyAll.add(bmsCompany);
        }
        ret.addEntry("list", companyAll);
        return ret;
    }
}

