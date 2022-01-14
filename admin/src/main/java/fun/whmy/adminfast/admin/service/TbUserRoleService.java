package fun.whmy.adminfast.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserRoleService extends IService<TbUserRoleBean> {

    List<String> getUserRoleCodesById(Long userId);

    boolean create(TbUserRoleBean role);

    int updateUserRole(Long userId, List<Long> roleIds);

    List<TbUserRoleBean> getRoleList(Long userId);

    Page<TbUserRoleBean> list(String keyword, Integer pageSize, Integer pageNum, Long companyId);

    List<TbUserResourceBean> listResource(Long roleId);

    int allocResource(Long roleId, List<Long> resourceIds);
}
