package fun.whmy.adminfast.admin.controller.template;

import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.whmy.adminfast.admin.controller.AbstractBaseController;
import fun.whmy.adminfast.admin.model.basic.CommonPage;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.constant.AdminRole;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * @version 1.0
 * @author： wanghanming
 * @date： 2022-01-20 11:05
 */

@AllArgsConstructor
@RestController
@RequestMapping("/rest/template")
public class TemplateController extends AbstractBaseController {

//    private TemplateService templateService;

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @GetMapping("/list")
    public ResultBean list(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        ResultBean ret = new ResultBean();
//        Page<Object> list = templateService.list(keyword, pageSize, pageNum);
//        CommonPage<Object> commonPage = CommonPage.restPage(list);
        CommonPage<Object> commonPage = new CommonPage<>();
        commonPage.setList(new LinkedList<>());
        commonPage.setPageNum(pageNum);
        commonPage.setPageSize(pageSize);
        commonPage.setTotal(0L);
        commonPage.setTotalPage(0);
        ret.addEntry("page", commonPage);
        return ret;
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/create")
    public ResultBean create(@RequestBody Object object) {
        //做相关参数验证
//        boolean success = templateService.save(object);
        boolean success = true;
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "提交的内容有冲突");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/update")
    public ResultBean update(@RequestBody Object object) {
        //做相关参数验证
//        boolean success = templateService.updateById(object);
        boolean success = true;
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "提交的内容有冲突");
    }

    @SaCheckPermission(AdminRole.SYSTEM_MANAGER_COMPANY)
    @PostMapping("/del")
    public ResultBean softDel(@RequestParam("id") Long id) {
//        boolean success = templateService.del(id);
        boolean success = true;
        if (success) {
            return success();
        }
        return fail(ErrorCode.ERROR, "删除失败");
    }
}
