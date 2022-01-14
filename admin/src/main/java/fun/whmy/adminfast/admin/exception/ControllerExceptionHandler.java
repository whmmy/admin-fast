package fun.whmy.adminfast.admin.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 捕获自定义异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public ResultBean customizeException(BaseException e) {
        ResultBean ret = new ResultBean();
        log.error("server catch exception:{}, exception:{}", e.getMessage(), e);
        ret.setCode(e.getCode());
        ret.setMsg(e.getMsg());
        return ret;
    }


    // 全局异常拦截（拦截项目中的NotLoginException异常）
    @ResponseBody
    @ExceptionHandler(NotLoginException.class)
    public ResultBean handlerNotLoginException(NotLoginException nle, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultBean ret = new ResultBean();
        // 打印堆栈，以供调试
        nle.printStackTrace();

        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        } else {
            message = "当前会话未登录";
        }

        ret.setCode(ErrorCode.USER_NOT_ONLICE);
        ret.setMsg(message);

        // 返回给前端
        return ret;
    }

    @ResponseBody
    @ExceptionHandler(NotPermissionException.class)
    public ResultBean handlerNotPermissionException(NotPermissionException nle, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultBean ret = new ResultBean();
        // 打印堆栈，以供调试
        nle.printStackTrace();

        ret.setCode(ErrorCode.USER_NOT_PERMISSION);
        ret.setMsg(nle.getMessage());

        // 返回给前端
        return ret;
    }
}
