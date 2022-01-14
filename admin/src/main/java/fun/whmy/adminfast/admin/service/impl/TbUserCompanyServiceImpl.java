package fun.whmy.adminfast.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.whmy.adminfast.admin.mapper.TbUserCompanyMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserCompanyBean;
import fun.whmy.adminfast.admin.service.TbUserCompanyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Service
public class TbUserCompanyServiceImpl extends ServiceImpl<TbUserCompanyMapper, TbUserCompanyBean> implements TbUserCompanyService {

    @Override
    public Page<TbUserCompanyBean> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<TbUserCompanyBean> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TbUserCompanyBean> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserCompanyBean> lambda = queryWrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.and(wrapper -> wrapper.like(TbUserCompanyBean::getName, keyword).or().eq(TbUserCompanyBean::getAddress, keyword));
        }
        lambda.eq(TbUserCompanyBean::getIsdelete, 0);
        return page(page, queryWrapper);
    }

    @Override
    public boolean createUnion(TbUserCompanyBean userCompanyBean) {
        //判断名称是否冲突
        QueryWrapper<TbUserCompanyBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserCompanyBean> lambda = wrapper.lambda();
        lambda.eq(TbUserCompanyBean::getName, userCompanyBean.getName());
        TbUserCompanyBean one = getOne(lambda);
        if (one != null) {
            return false;
        }
        return save(userCompanyBean);
    }

    @Override
    public boolean softDel(Long id) {
        TbUserCompanyBean sqlBean = new TbUserCompanyBean();
        sqlBean.setId(id);
        sqlBean.setIsdelete(1);
        return updateById(sqlBean);
    }
}
