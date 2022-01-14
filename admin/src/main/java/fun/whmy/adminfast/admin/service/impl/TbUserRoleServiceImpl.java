package fun.whmy.adminfast.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.whmy.adminfast.admin.mapper.TbUserRoleMapper;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserResourceBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleRelationBean;
import fun.whmy.adminfast.admin.model.bean.admin.TbUserRoleResourceRelationBean;
import fun.whmy.adminfast.admin.service.TbUserResourceService;
import fun.whmy.adminfast.admin.service.TbUserRoleRelationService;
import fun.whmy.adminfast.admin.service.TbUserRoleResourceRelationService;
import fun.whmy.adminfast.admin.service.TbUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author whmy
 * @since 2022-01-12
 */
@Service
public class TbUserRoleServiceImpl extends ServiceImpl<TbUserRoleMapper, TbUserRoleBean> implements TbUserRoleService {

    @Autowired
    private TbUserRoleRelationService roleRelationService;

    @Autowired
    private TbUserResourceService resourceService;

    @Autowired
    private TbUserRoleResourceRelationService roleResourceRelationService;

    @Override
    public List<String> getUserRoleCodesById(Long userId) {
        return baseMapper.readRoleCodesByUserId(userId);
    }

    @Override
    public boolean create(TbUserRoleBean role) {
        TbUserRoleBean sqlBean = new TbUserRoleBean();
        sqlBean.setCompanyId(role.getCompanyId());
        sqlBean.setCode(role.getCode());
        sqlBean.setName(role.getName());
        sqlBean.setDescription(role.getDescription());
        sqlBean.setCreateTime(new Date());
        sqlBean.setAdminCount(0);
        sqlBean.setSort(0);
        return save(role);
    }

    @Override
    public int updateUserRole(Long userId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        QueryWrapper<TbUserRoleRelationBean> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TbUserRoleRelationBean::getUserId, userId);
        roleRelationService.remove(wrapper);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<TbUserRoleRelationBean> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                TbUserRoleRelationBean roleRelation = new TbUserRoleRelationBean();
                roleRelation.setUserId(userId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            roleRelationService.saveBatch(list);
        }
        return count;
    }

    @Override
    public List<TbUserRoleBean> getRoleList(Long userId) {
        return baseMapper.getRoleList(userId);
    }

    @Override
    public Page<TbUserRoleBean> list(String keyword, Integer pageSize, Integer pageNum, Long companyId) {
        Page<TbUserRoleBean> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TbUserRoleBean> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TbUserRoleBean> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(TbUserRoleBean::getName, keyword);
        }
        lambda.eq(TbUserRoleBean::getCompanyId, companyId);
        return page(page, wrapper);
    }

    @Override
    public List<TbUserResourceBean> listResource(Long roleId) {
        return resourceService.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        QueryWrapper<TbUserRoleResourceRelationBean> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TbUserRoleResourceRelationBean::getRoleId, roleId);
        roleResourceRelationService.remove(wrapper);
        //批量插入新关系
        List<TbUserRoleResourceRelationBean> relationList = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            TbUserRoleResourceRelationBean relation = new TbUserRoleResourceRelationBean();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            relationList.add(relation);
        }
        roleResourceRelationService.saveBatch(relationList);
        return resourceIds.size();
    }
}
