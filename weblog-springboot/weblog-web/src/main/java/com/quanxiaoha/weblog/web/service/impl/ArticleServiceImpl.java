package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.quanxiaoha.weblog.common.domain.dos.*;
import com.quanxiaoha.weblog.common.domain.mapper.*;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.web.convert.ArticleConvert;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import com.quanxiaoha.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.quanxiaoha.weblog.web.model.vo.tag.FindTagListRspVO;
import com.quanxiaoha.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleConvert articleConvert;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;


    /**
     * 分页查询首页信息
     * @param findIndexArticlePageListReqVO
     * @return
     */
    @Override
    public PageResponse findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        Long current = findIndexArticlePageListReqVO.getCurrent();
        Long size = findIndexArticlePageListReqVO.getSize();

        //分页查询,获取响应结果
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, null, null, null);
        List<ArticleDO> records = articleDOPage.getRecords();

        //DO对象转VO,封装返回对象
        List<FindIndexArticlePageListRspVO> vos = null;
        if(!CollectionUtils.isEmpty(records)){
            //DO转对应的VO对象
            vos = records.stream()
                    .map(articleDO -> ArticleConvert.INSTANCE.convertArticleDoToFindIndexArticlePageListRspVO(articleDO))
                    .collect(Collectors.toList());
        }

        //获取所有文章id
        List<ArticleDO> articleDOS = articleMapper.selectList(Wrappers.emptyWrapper());
        List<Long> ids = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());


        //1.设置文章的分类信息
        //查找所有的分类id
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
        //将id,name封装起来
        Map<Long, String> categoryIdNameMap = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));
        //查询出所有的文章分类关联信息
        List<ArticleCategoryRelDO> categoryRelDOS = articleCategoryRelMapper.selectList(Wrappers.emptyWrapper());

        if(vos != null){
            //对vos中的每个文章进行考虑
            vos.forEach(
                    vo -> {
                        Long currArticleId = vo.getId();
                        //过滤出当前文章的信息
                        Optional<ArticleCategoryRelDO> optional = categoryRelDOS.stream().filter(crl -> Objects.equals(crl.getArticleId(), currArticleId)).findAny();
                        //存在进行下一步判断
                        if(optional.isPresent()){
                            ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                            Long articleId = articleCategoryRelDO.getArticleId();
                            String name = categoryIdNameMap.get(articleCategoryRelDO.getCategoryId());

                            FindCategoryListRspVO findCategoryListRspVO = FindCategoryListRspVO.builder()
                                    .id(articleId)
                                    .name(name)
                                    .build();
                            //设置到当前vo中
                            vo.setFindCategoryListRspVO(findCategoryListRspVO);
                        }
                    }
            );

            //2.设置文章的标签信息
            //查询所有的标签id
            List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());
            //将id和name封装起来
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));
            //查询出所有的文章标签关联信息
            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectList(Wrappers.emptyWrapper());
            vos.forEach(
                    vo -> {
                        Long currArticleId = vo.getId();
                        //过滤出当前的文章
                        List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDOS.stream()
                                .filter(crl -> Objects.equals(crl.getArticleId(), currArticleId))
                                .collect(Collectors.toList());

                        //封装返回信息
                        List<FindTagListRspVO> findTagListRspVOS = Lists.newArrayList();
                        articleTagRelDOList.forEach(
                                articleTagRelDO -> {
                                    //标签id和对应name
                                    Long articleId = articleTagRelDO.getArticleId();
                                    String name = tagIdNameMap.get(articleTagRelDO.getTagId());

                                    FindTagListRspVO build = FindTagListRspVO.builder()
                                            .id(articleId)
                                            .name(name)
                                            .build();

                                    findTagListRspVOS.add(build);
                                });
                        vo.setFindTagListRspVOList(findTagListRspVOS);
                    }
            );


        }
        return PageResponse.success(articleDOPage,vos);
    }

}
