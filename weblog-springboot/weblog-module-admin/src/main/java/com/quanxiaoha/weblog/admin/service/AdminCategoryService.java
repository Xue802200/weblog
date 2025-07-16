package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.quanxiaoha.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.quanxiaoha.weblog.common.utils.PageResponse;
import com.quanxiaoha.weblog.common.utils.Response;

public interface AdminCategoryService {

    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);


    /**
     * 分页分类数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findCategoryList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /**
     * 删除分类
     * @param deleteCategoryReqVO  分类id
     * @return
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 下拉查询所有的分类消息
     * @return
     */
    Response findCategorySelectList();
}
