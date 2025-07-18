package com.quanxiaoha.weblog.admin.convert;

import com.quanxiaoha.weblog.admin.model.vo.atricle.FindArticleDetailReqVO;
import com.quanxiaoha.weblog.admin.model.vo.atricle.FindArticleDetailRspVO;
import com.quanxiaoha.weblog.admin.model.vo.atricle.PublishArticleReqVO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleConvert {

    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    //将VO对象转为ArticleDO
    ArticleDO convertReqToArticleDO(PublishArticleReqVO publishArticleReqVO);

    FindArticleDetailRspVO convertArticleDetailRspVO(ArticleDO articleDO);
}
