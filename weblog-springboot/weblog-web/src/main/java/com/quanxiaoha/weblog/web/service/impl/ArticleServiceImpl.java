package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.admin.model.vo.atricle.FindArticlePageListRspVO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleMapper;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.web.convert.ArticleConvert;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import com.quanxiaoha.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleConvert articleConvert;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public PageResponse findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        Long current = findIndexArticlePageListReqVO.getCurrent();
        Long size = findIndexArticlePageListReqVO.getSize();

        //分页查询查询article的主体
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, null, null, null);

        //获取查询到的分页数据
        List<ArticleDO> articleDOS = articleDOPage.getRecords();

        List<FindIndexArticlePageListRspVO> vos = null;
        if(!CollectionUtils.isEmpty(articleDOS)){
            //DO转VO,封装返回对象
            vos = articleDOS.stream()
                    .map(articleConvert::convertArticleDoToFindIndexArticlePageListRspVO)
                    .collect(Collectors.toList());


            //拿取每一篇的文章id
            List<Long> idList = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());

            //设置分类category
            //查询出所有的分类
            List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
            //转Map,方便后续根据分类categoryId拿到对应的名称name
            Map<Long, String> map = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));
            //查询出所有的分类记录
            List<ArticleCategoryRelDO> categoryRelDOS = articleCategoryRelMapper.selectByArticleIds(idList);

            //针对每一篇文章,封装对应的属性
            vos.forEach(
                    articleCategoryRelDO -> {

                    }
            );

        }

        return null;
    }
}
