package com.quanxiaoha.weblog.web.service;

import com.quanxiaoha.weblog.admin.model.vo.atricle.FindArticlePageListRspVO;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.web.model.vo.article.FindArticleDetailReqVO;
import com.quanxiaoha.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;

public interface ArticleService {
    /**
     * 首页查询文章
     * @param findIndexArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);


    /**
     * 首页查询文章详情信息
     * @param findArticleDetailReqVO
     * @return
     */
    Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);
}
