package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanxiaoha.weblog.admin.convert.BlogSettingsConvert;
import com.quanxiaoha.weblog.admin.model.vo.blogsettings.FindBlogSettingsRspVO;
import com.quanxiaoha.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.quanxiaoha.weblog.admin.service.AdminBlogSettingsService;
import com.quanxiaoha.weblog.common.domain.dos.BlogSettingsDO;
import com.quanxiaoha.weblog.common.domain.mapper.BlogSettingsMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import org.springframework.stereotype.Service;

@Service
public class AdminBlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper,BlogSettingsDO> implements AdminBlogSettingsService {

    /**
     * 更新博客信息
     * @param updateBlogSettingsReqVO
     * @return
     */
    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {

        // VO 转 DO
        BlogSettingsDO blogSettingsDO = BlogSettingsConvert.INSTANCE.convertVOToDO(updateBlogSettingsReqVO);
        blogSettingsDO.setId(1L);

        // 保存或更新（当数据库中存在 ID 为 1 的记录时，则执行更新操作，否则执行插入操作）
        saveOrUpdate(blogSettingsDO);
        return Response.success();
    }

    /**
     * 查询详情信息
     * @return
     */
    @Override
    public Response findDetail() {
        //查询当前记录
        BlogSettingsDO blogSettingsDO = getById(1L);

        //DO转VO返回
        FindBlogSettingsRspVO findBlogSettingsRspVO = BlogSettingsConvert.INSTANCE.convertDOToVO(blogSettingsDO);

        return Response.success(findBlogSettingsRspVO);
    }
}
