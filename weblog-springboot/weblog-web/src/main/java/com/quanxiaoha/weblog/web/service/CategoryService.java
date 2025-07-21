package com.quanxiaoha.weblog.web.service;

import com.quanxiaoha.weblog.common.utils.Response;

public interface CategoryService {

    /**
     * 获取分类列表
     * @return
     */
    Response findCategoryList();
}
