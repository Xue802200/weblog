package com.quanxiaoha.weblog.web.service.impl;

import com.quanxiaoha.weblog.common.domain.dos.BlogSettingsDO;
import com.quanxiaoha.weblog.common.domain.mapper.BlogSettingsMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.web.convert.BlogSettingsConvert;
import com.quanxiaoha.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import com.quanxiaoha.weblog.web.service.BlogSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogSettingsServiceImpl implements BlogSettingsService {

    @Autowired
    private BlogSettingsMapper blogSettingsMapper;


    @Override
    public Response findDetail() {
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);

        //DO转VO
        FindBlogSettingsDetailRspVO findBlogSettingsDetailRspVO = BlogSettingsConvert.INSTANCE.convertDOToVO(blogSettingsDO);

        return Response.success(findBlogSettingsDetailRspVO);
    }
}
