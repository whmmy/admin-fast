package fun.whmy.adminfast.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserCompanyBean;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserCompanyService extends IService<TbUserCompanyBean> {

    Page<TbUserCompanyBean> list(String keyword, Integer pageSize, Integer pageNum);

    boolean createUnion(TbUserCompanyBean userCompanyBean);

    boolean softDel(Long id);
}
