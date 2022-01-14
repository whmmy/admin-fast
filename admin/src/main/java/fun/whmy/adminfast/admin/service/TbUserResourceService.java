package fun.whmy.adminfast.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;

import java.util.List;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserResourceService extends IService<TbUserResourceBean> {
    List<TbUserResourceBean> getUserResourceById(Long userId);

    List<String> getUserResourceCodesById(Long loginId);

    List<TbUserResourceBean> getResourceListByRoleId(Long roleId);

    boolean create(TbUserResourceBean userResourceBean);

    boolean updateUnion(TbUserResourceBean userResourceBean);

    Page<TbUserResourceBean> list(Long categoryId, String nameKeyword, String codeKeyword, Integer pageSize, Integer pageNum);
}
