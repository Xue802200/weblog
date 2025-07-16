package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.quanxiaoha.weblog.admin.service.AdminCategoryService;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.enums.ResponseCodeEnum;
import com.quanxiaoha.weblog.common.exception.BizException;
import com.quanxiaoha.weblog.common.model.vo.SelectRspVO;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryMapper  categoryMapper;


    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */
    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        String categoryName = addCategoryReqVO.getName();

        //先判断分类是否存在
        CategoryDO categoryDO = categoryMapper.selectByName(categoryName);

        if(Objects.nonNull(categoryDO)){
            log.warn("分类名称：{},此分类已经存在",categoryName);
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        //构建DO类
        CategoryDO insertCategoryDO = CategoryDO.builder()
                .name(addCategoryReqVO.getName().trim())
                .build();

        //执行insert
        categoryMapper.insert(insertCategoryDO);

        return Response.success("添加分类成功");
    }


    /**
     * 分类分页数据查询
     * @param findCategoryPageListReqVO  入参的VO对象
     * @return                  封装的PageResponse对象
     */
    @Override
    public PageResponse findCategoryList(FindCategoryPageListReqVO  findCategoryPageListReqVO) {
        //获取当前页,每页所展示的数据量
        Long current = findCategoryPageListReqVO.getCurrent();
        Long size = findCategoryPageListReqVO.getSize();

        //分页对象
        Page<CategoryDO> page = new Page<>(current, size);

        //构建查询条件
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();

        String name = findCategoryPageListReqVO.getName();
        LocalDate startDate = findCategoryPageListReqVO.getStartDate();
        LocalDate endDate = findCategoryPageListReqVO.getEndDate();

        wrapper
                .like(StringUtils.isNotBlank(name), CategoryDO::getName, name)   //模糊匹配
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)  //大于起始时间
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)      //小于最大时间
                .orderByDesc(CategoryDO::getCreateTime); //创建时间倒叙排序

        //执行分页查询
        Page<CategoryDO> categoryDOPage = categoryMapper.selectPage(page, wrapper);

        List<CategoryDO> categoryDOS = categoryDOPage.getRecords();

        //DO转VO
        List<FindCategoryPageListRspVO> vos = null;
        if(!CollectionUtils.isEmpty(categoryDOS)){
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryPageListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .createTime(categoryDO.getCreateTime())
                                    .build())
                    .collect(Collectors.toList());
        }
        return PageResponse.success(page,vos);
    }


    /**
     * 删除分类
     * @param deleteCategoryReqVO  分类id
     * @return
     */
    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        //获取分类id
        Long categoryId = deleteCategoryReqVO.getId();

        //执行删除逻辑
        int i = categoryMapper.deleteById(categoryId);

        if(i == 0){
            return Response.fail("删除失败,不存在这个id");
        }

        return Response.success("删除成功");
    }

    /**
     * 下拉查询所有的分类消息
     * @return
     */
    @Override
    public Response findCategorySelectList() {
        List<CategoryDO> categoryDOS = categoryMapper.selectList(null);

        //DO转VO
        List<SelectRspVO> selectRspVOS = new ArrayList<>();

        if(!CollectionUtils.isEmpty(categoryDOS)){
            selectRspVOS = categoryDOS.stream()
                    .map(categoryDO -> SelectRspVO.builder()
                            .label(categoryDO.getName())
                            .value(categoryDO.getId())
                            .build()).collect(Collectors.toList());
        }
        return Response.success(selectRspVOS);
    }
}
