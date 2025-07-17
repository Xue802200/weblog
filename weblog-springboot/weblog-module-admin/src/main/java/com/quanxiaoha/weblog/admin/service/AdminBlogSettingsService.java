package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.quanxiaoha.weblog.common.utils.Response;

public interface AdminBlogSettingsService {

    /**
     * 更新博客信息
     * @param updateBlogSettingsReqVO
     * @return
     */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 查询详情信息
     * @return
     */
    Response findDetail();
}
