package com.quanxiaoha.weblog.web.convert;

import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticleConvert {

    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    /**
     * 将ArticleDO转换到FindIndexArticlePageListRspVO
     * @param articleDO
     * @return
     */
    FindIndexArticlePageListRspVO convertArticleDoToFindIndexArticlePageListRspVO(ArticleDO articleDO);
}
