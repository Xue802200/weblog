package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.ArticleContentDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;

public interface ArticleContentMapper extends BaseMapper<ArticleContentDO> {

    /**
     * 根据文章 ID 删除记录
     * @param articleId
     * @return
     */
    default int deleteByArticleId(Long articleId) {
        return delete(Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId, articleId));
    }

    /**
     * 根据文章id获取文章内容
     * @param articleId
     * @return
     */
    default ArticleContentDO selectByArticleId(Long articleId){
        return selectOne(Wrappers.<ArticleContentDO>lambdaQuery()
        .eq(ArticleContentDO::getArticleId, articleId));
    }

    /**
     * 根据文章id给内容更新
     * @param articleContentDO
     * @return
     */
    default int updateByArticleContentDO(ArticleContentDO articleContentDO){
        return update(articleContentDO,Wrappers.<ArticleContentDO>lambdaUpdate()
                .eq(ArticleContentDO::getArticleId, articleContentDO.getArticleId()));
    }

}
