package fun.whmy.adminfast.admin.mapper;

import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserRoleMapper extends BaseMapper<TbUserRoleBean> {

    List<String> readRoleCodesByUserId(Long userId);

    List<TbUserRoleBean> getRoleList(Long userId);
}
