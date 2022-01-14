package fun.whmy.adminfast.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBeanExtend;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserMapper extends BaseMapper<TbUserBean> {


    Page<TbUserBeanExtend> readExtendList(@Param("companyId") Long companyId, @Param("keyword") String keyword, Page<TbUserBean> page);
}
