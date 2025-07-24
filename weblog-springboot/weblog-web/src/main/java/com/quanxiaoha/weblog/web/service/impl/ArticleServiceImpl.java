package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.quanxiaoha.weblog.admin.event.ReadArticleEvent;
import com.quanxiaoha.weblog.common.domain.dos.*;
import com.quanxiaoha.weblog.common.domain.mapper.*;
import com.quanxiaoha.weblog.common.enums.ResponseCodeEnum;
import com.quanxiaoha.weblog.common.exception.BizException;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.web.convert.ArticleConvert;
import com.quanxiaoha.weblog.web.markdown.MarkdownHelper;
import com.quanxiaoha.weblog.web.model.vo.article.*;
import com.quanxiaoha.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.quanxiaoha.weblog.web.model.vo.tag.FindTagListRspVO;
import com.quanxiaoha.weblog.web.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 获取首页文章分页数据
     *
     * @param findIndexArticlePageListReqVO
     * @return
     */
    @Override
    public Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO) {
        Long current = findIndexArticlePageListReqVO.getCurrent();
        Long size = findIndexArticlePageListReqVO.getSize();

        // 第一步：分页查询文章主体记录
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, null, null, null);

        // 返回的分页数据
        List<ArticleDO> articleDOS = articleDOPage.getRecords();

        List<FindIndexArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            // 文章 DO 转 VO
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertArticleDoToFindIndexArticlePageListRspVO)
                    .collect(Collectors.toList());

            // 拿到所有文章的 ID 集合
            List<Long> articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());

            // 第二步：设置文章所属分类
            // 查询所有分类
            List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
            // 转 Map, 方便后续根据分类 ID 拿到对应的分类名称
            Map<Long, String> categoryIdNameMap = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            // 根据文章 ID 批量查询所有关联记录
            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectByArticleIds(articleIds);

            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤出当前文章对应的关联数据
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).findAny();

                // 若不为空
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    // 通过分类 ID 从 map 中拿到对应的分类名称
                    String categoryName = categoryIdNameMap.get(categoryId);

                    FindCategoryListRspVO findCategoryListRspVO = FindCategoryListRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    // 设置到当前 vo 类中
                    vo.setCategory(findCategoryListRspVO);
                }
            });

            // 第三步：设置文章标签
            // 查询所有标签
            List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());
            // 转 Map, 方便后续根据标签 ID 拿到对应的标签名称
            Map<Long, String> mapIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            // 拿到所有文章的标签关联记录
            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleIds(articleIds);
            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤出当前文章的标签关联记录
                List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).collect(Collectors.toList());

                List<FindTagListRspVO> findTagListRspVOS = Lists.newArrayList();
                // 将关联记录 DO 转 VO, 并设置对应的标签名称
                articleTagRelDOList.forEach(articleTagRelDO -> {
                    Long tagId = articleTagRelDO.getTagId();
                    String tagName = mapIdNameMap.get(tagId);

                    FindTagListRspVO findTagListRspVO = FindTagListRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    findTagListRspVOS.add(findTagListRspVO);
                });
                // 设置转换后的标签数据
              vo.setTags(findTagListRspVOS);
            });
        }

        return PageResponse.success(articleDOPage, vos);
    }

    /**
     * 查询文章详情页
     * @param findArticleDetailReqVO
     * @return
     */
    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long articleId = findArticleDetailReqVO.getArticleId();

        //查询对应的文章
        ArticleDO articleDO = articleMapper.selectById(articleId);

        //判断文章是否存在
        if(Objects.isNull(articleDO)){
            log.info("文章不存在,articleId:{}",articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        //查询正文
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        //DO转VO
        FindArticleDetailRspVO findArticleDetailRspVO = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .content(MarkdownHelper.convertMarkdown2Html(articleContentDO.getContent()))
                .createTime(articleDO.getCreateTime())
                .readNum(articleDO.getReadNum()).build();

        //封装文章的分类categoryID和分类categoryName
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        Long categoryId = articleCategoryRelDO.getCategoryId();
        findArticleDetailRspVO.setCategoryId(categoryId);
        findArticleDetailRspVO.setCategoryName(categoryMapper.selectById(categoryId).getName());

        //封装标签集合
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());

        List<TagDO> tagDOS = tagMapper.selectByIds(tagIds);
        List<FindTagListRspVO> findTagListRspVOS = Lists.newArrayList();

        //文章存在标签,进行下一步操作
        if(!CollectionUtils.isEmpty(tagDOS)){
            //DO转VO用于封装文章的标签信息
            tagDOS.stream().forEach(tagDO -> {
                FindTagListRspVO findTagListRspVO = FindTagListRspVO.builder()
                        .id(tagDO.getId())
                        .name(tagDO.getName()).build();
                findTagListRspVOS.add(findTagListRspVO);
            });
        }
        findArticleDetailRspVO.setTags(findTagListRspVOS);

        //查询上一篇文章
        ArticleDO formerArticle = articleMapper.selectPreArticle(articleId);
        FindPreNextArticleRspVO formerArticleRsp = null;
        if (Objects.nonNull(formerArticle)) {
            formerArticleRsp = FindPreNextArticleRspVO.builder()
                    .articleId(formerArticle.getId())
                    .articleTitle(formerArticle.getTitle()).build();
        }

        //查询下一篇文章
        ArticleDO nextArticle = articleMapper.selectNextArticle(articleId);
        FindPreNextArticleRspVO nextArticleRspVO = null;
        if (Objects.nonNull(nextArticle)) {
            nextArticleRspVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticle.getId())
                    .articleTitle(nextArticle.getTitle()).build();
        }

        //封装
        findArticleDetailRspVO.setNextArticle(nextArticleRspVO);
        findArticleDetailRspVO.setPreArticle(formerArticleRsp);

        //发布文章阅读事件
        applicationEventPublisher.publishEvent(new ReadArticleEvent(this,articleId));

        return Response.success(findArticleDetailRspVO);
    }
}
