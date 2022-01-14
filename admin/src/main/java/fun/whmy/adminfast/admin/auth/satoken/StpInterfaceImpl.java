package fun.whmy.adminfast.admin.auth.satoken;

import cn.dev33.satoken.stp.StpInterface;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 自定义权限验证接口扩展
 * @Date: 2021/4/7 8:15
 */
@Component
@AllArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private TbUserResourceService resourceService;

    private TbUserRoleService roleService;

    /**
     * 返回账号拥有的权限码
     *
     * @param loginId
     * @param loginKey
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {

        return resourceService.getUserResourceCodesById(Long.valueOf((String) loginId));
    }

    /**
     * 返回账号拥有的角色标识
     *
     * @param loginId
     * @param loginKey
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        return roleService.getUserRoleCodesById(Long.valueOf((String) loginId));
    }


}
