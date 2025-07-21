package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.quanxiaoha.weblog.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类列表
     * @return
     */
    @Override
    public Response findCategoryList() {
        //查询出所有的分类列表
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());

        //封装返参类型
        List<FindCategoryListRspVO> vos = null;
        if(CollectionUtils.isNotEmpty(categoryDOS)){
            vos = categoryDOS.stream()
                    .map(categoryDO ->  FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName()).build()).collect(Collectors.toList());
        }

        return Response.success(vos);
    }
}
