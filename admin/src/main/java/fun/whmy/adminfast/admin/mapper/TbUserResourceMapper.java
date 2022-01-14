package fun.whmy.adminfast.admin.mapper;

import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;

import java.util.List;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserResourceMapper extends BaseMapper<TbUserResourceBean> {

    List<String> getResourceList(Long userId);

    List<String> getAllResourceList();

    List<TbUserResourceBean> getResourceBeanList(Long userId);

    List<TbUserResourceBean> getResourceListByRoleId(Long roleId);
}
