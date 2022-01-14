package fun.whmy.adminfast.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.whmy.adminfast.admin.mapper.TbUserMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserBeanExtend;
import fun.whmy.adminfast.admin.service.TbUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUserBean> implements TbUserService {

    @Override
    public Page<TbUserBean> list(Long companyId, String keyword, Integer pageSize, Integer pageNum) {
        Page<TbUserBean> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TbUserBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserBean> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(TbUserBean::getUserName, keyword);
        }
        if (companyId != 0) {
            lambda.eq(TbUserBean::getCompanyId, companyId);
        }
        lambda.eq(TbUserBean::getIsDeleted, 0);
        return page(page, wrapper);
    }

    @Override
    public Page<TbUserBeanExtend> listExtend(Long companyId, String keyword, Integer pageSize, Integer pageNum) {
        Page<TbUserBean> page = new Page<>(pageNum, pageSize);
        return baseMapper.readExtendList(companyId, keyword, page);
    }

    @Override
    public TbUserBean getUserByName(String userName) {
        QueryWrapper<TbUserBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserBean> lambda = wrapper.lambda();
        lambda.eq(TbUserBean::getUserName, userName);
        lambda.eq(TbUserBean::getIsDeleted, 0);
        return getOne(wrapper);
    }

    @Override
    public boolean addUser(TbUserBean userBean) {
        TbUserBean sqlBean = new TbUserBean();
        sqlBean.setIsDeleted(0);
        sqlBean.setPassword(userBean.getPassword());
        sqlBean.setUserName(userBean.getUserName());
        sqlBean.setCreateId(userBean.getCreateId());
        sqlBean.setUpdateId(userBean.getUpdateId());
        sqlBean.setCompanyId(userBean.getCompanyId());
        sqlBean.setEmail(userBean.getEmail());
        sqlBean.setPhone(userBean.getPhone());
        sqlBean.setToken(userBean.getToken());
        sqlBean.setRemark(userBean.getRemark());
        return save(sqlBean);
    }

    @Override
    public boolean editUser(TbUserBean userBean) {
        TbUserBean sqlBean = new TbUserBean();
        sqlBean.setUserId(userBean.getUserId());
        sqlBean.setPassword(userBean.getPassword());
        sqlBean.setUserName(userBean.getUserName());
        sqlBean.setUpdateId(userBean.getUpdateId());
        sqlBean.setCompanyId(userBean.getCompanyId());
        sqlBean.setEmail(userBean.getEmail());
        sqlBean.setPhone(userBean.getPhone());
        sqlBean.setToken(userBean.getToken());
        sqlBean.setRemark(userBean.getRemark());
        return updateById(sqlBean);
    }

    @Override
    public boolean deleteById(Long id, Long userId) {
        TbUserBean sqlBean = new TbUserBean();
        sqlBean.setIsDeleted(1);
        sqlBean.setUserId(id);
        return updateById(sqlBean);
    }
}
