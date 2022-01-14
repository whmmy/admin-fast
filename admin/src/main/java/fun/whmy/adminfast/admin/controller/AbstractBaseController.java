package fun.whmy.adminfast.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;

/**
 * @version 1.0
 * @author： wanghanming
 * @date： 2020-12-21 09:42
 */
public abstract class AbstractBaseController {

    protected Long getCurrentUserId() {
        return Long.valueOf((String) StpUtil.getLoginId());
    }

    protected Long getCurrentCompanyId() {
        return StpUtil.getSession().getLong("companyId");
    }

    protected ResultBean success() {
        return new ResultBean();
    }

    protected ResultBean fail() {
        final ResultBean ret = new ResultBean();
        ret.setCode(ErrorCode.ERROR);
        return ret;
    }

    protected ResultBean fail(Integer code, String msg) {
        final ResultBean ret = new ResultBean();
        ret.setCode(code);
        ret.setMsg(msg);
        return ret;
    }

    protected ResultBean fail(String msg) {
        final ResultBean ret = new ResultBean();
        ret.setCode(ErrorCode.ERROR);
        ret.setMsg(msg);
        return ret;
    }
}
