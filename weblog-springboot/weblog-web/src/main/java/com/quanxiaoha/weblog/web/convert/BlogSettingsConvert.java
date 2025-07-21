package com.quanxiaoha.weblog.web.convert;

import com.quanxiaoha.weblog.common.domain.dos.BlogSettingsDO;
import com.quanxiaoha.weblog.web.model.vo.blogsettings.FindBlogSettingsDetailRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogSettingsConvert {

    /**
     * 获取转换器实例对象
     */
    BlogSettingsConvert INSTANCE = Mappers.getMapper(BlogSettingsConvert.class);

    FindBlogSettingsDetailRspVO convertDOToVO(BlogSettingsDO blogSettingsDO);
}
