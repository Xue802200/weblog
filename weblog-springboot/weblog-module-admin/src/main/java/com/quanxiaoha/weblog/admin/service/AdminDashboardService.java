package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.common.utils.Response;

public interface AdminDashboardService {

    /**
     * 获取仪表盘基础统计信息
     * @return
     */
    Response findDashboardStatistics();


    /**
     * 获取文章热点详情的信息
     * @return
     */
    Response findDashboardPublishArticleStatistics();


    /**
     * 查询近七天文章的浏览量
     * @return
     */
    Response findDashboardPVStatistics();
}
