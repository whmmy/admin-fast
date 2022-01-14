package fun.whmy.adminfast.admin.controller.admin;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.whmy.adminfast.admin.model.basic.ResultBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBean;
import fun.whmy.adminfast.admin.model.constant.ErrorCode;
import fun.whmy.adminfast.admin.model.dto.MsLoginParam;
import fun.whmy.adminfast.admin.service.TbUserService;
import fun.whmy.adminfast.admin.utils.PublicKeyMap;
import fun.whmy.adminfast.admin.utils.RSAUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @author： wanghanming
 * @date： 2022-01-12 14:58
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/rest/auth")
public class LoginController {

    private TbUserService userService;


    @GetMapping("/keypair")
    @ResponseBody
    public PublicKeyMap keyPair() {
        return RSAUtils.getPublicKeyMap();
    }

    @PostMapping("/login/{module}")
    public ResultBean login(@RequestBody MsLoginParam user, @PathVariable String module) {
        ResultBean ret = new ResultBean();
        if (user == null) {
            log.error("login，login error bean is null");
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("登录错误，请输入完整登录信息");
            return ret;
        }

        if (StrUtil.isBlank(user.getUserName()) || StrUtil.isBlank(user.getPassword())) {
            log.error("login，login error username|psw is null");
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("登录错误，请输入用户名或密码为空");
            return ret;
        }

        QueryWrapper<TbUserBean> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserBean> lambda = queryWrapper.lambda();
        lambda.eq(TbUserBean::getUserName, user.getUserName());
        TbUserBean sysUser = userService.getOne(queryWrapper);
        if (sysUser == null) {
            log.error("login，login error userName is error");
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("登录错误，当前用户名或密码错误");
            return ret;
        }

        String password = RSAUtils.decryptStringByJs(user.getPassword(), module);

        //判断密码是否正确
//        String checkPsw = LoginUtils.decodePsw(sysUser.getPassword());
//        String inputPsw = LoginUtils.decodePsw(user.getPassword());

        if (!password.equals(sysUser.getPassword())) {
            log.error("login，login error password is error");
            ret.setCode(ErrorCode.ERROR);
            ret.setMsg("登录错误，当前用户名或密码错误");
            return ret;
        }
//        String token = LoginUtils.generateToken();
//        OnlineUserInfo userInfo = OnlineUserInfo.builder().openId(sysUser.getRemark()).userId(sysUser.getUserId()).userRole(sysUser.getRole()).clientType(2).build();
//        MsUserData.setUserInfoToRedis(token, userInfo);

        StpUtil.login(sysUser.getUserId());
        final SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        final SaSession session = StpUtil.getSession();
        session.set("companyId",sysUser.getCompanyId());
        ret.addEntry("userName", sysUser.getUserName());
        ret.addEntry("companyId", sysUser.getCompanyId());
        ret.addEntry("token", tokenInfo.getTokenValue());
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    public ResultBean logout() {
        ResultBean ret = new ResultBean();
        StpUtil.logout();
        return ret;
    }
}
