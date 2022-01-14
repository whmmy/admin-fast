package fun.whmy.adminfast.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.whmy.adminfast.admin.mapper.TbUserResourceMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Service
public class TbUserResourceServiceImpl extends ServiceImpl<TbUserResourceMapper, TbUserResourceBean> implements TbUserResourceService {
    @Autowired
    private TbUserRoleService userRoleService;

    @Override
    public List<String> getUserResourceCodesById(Long userId) {
        //当用户角色为admin时则查看所有
        final List<String> codes = userRoleService.getUserRoleCodesById(userId);
        if (codes.contains("admin")) {
            return baseMapper.getAllResourceList();
        }
        return baseMapper.getResourceList(userId);
    }

    @Override
    public List<TbUserResourceBean> getResourceListByRoleId(Long roleId) {
        return baseMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public boolean create(TbUserResourceBean userResourceBean) {
        return save(userResourceBean);
    }

    @Override
    public boolean updateUnion(TbUserResourceBean userResourceBean) {
        TbUserResourceBean sqlBean = new TbUserResourceBean();
        sqlBean.setId(userResourceBean.getId());
        sqlBean.setDescription(userResourceBean.getDescription());
        sqlBean.setName(userResourceBean.getName());
        sqlBean.setSort(userResourceBean.getSort());
//        sqlBean.setCode(userResourceBean.getCode());
        boolean success = updateById(sqlBean);
        return success;
    }

    @Override
    public Page<TbUserResourceBean> list(Long categoryId, String nameKeyword, String codeKeyword, Integer pageSize, Integer pageNum) {
        Page<TbUserResourceBean> page = new Page<>(pageNum,pageSize);
        QueryWrapper<TbUserResourceBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserResourceBean> lambda = wrapper.lambda();
        if(categoryId!=null){
            lambda.eq(TbUserResourceBean::getCategoryId,categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            lambda.like(TbUserResourceBean::getName,nameKeyword);
        }
        if(StrUtil.isNotEmpty(codeKeyword)){
            lambda.like(TbUserResourceBean::getCode,codeKeyword);
        }
        lambda.eq(TbUserResourceBean::getIsdelete,0);

        return page(page,wrapper);
    }

    @Override
    public List<TbUserResourceBean> getUserResourceById(Long userId) {
        return baseMapper.getResourceBeanList(userId);
    }
}
