package fun.whmy.adminfast.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBean;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBeanExtend;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
public interface TbUserService extends IService<TbUserBean> {
    /**
     * 根据用户名或昵称分页查询用户
     */
    Page<TbUserBean> list(Long companyId, String keyword, Integer pageSize, Integer pageNum);
    Page<TbUserBeanExtend> listExtend(Long companyId, String keyword, Integer pageSize, Integer pageNum);

    TbUserBean getUserByName(String userName);

    boolean addUser(TbUserBean userBean);
    boolean editUser(TbUserBean userBean);

    boolean deleteById(Long id, Long userId);
}
