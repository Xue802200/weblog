package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.quanxiaoha.weblog.admin.service.AdminBlogSettingsService;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import com.quanxiaoha.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/blog/settings")
@Api(tags = "Admin 博客设置模块")
public class AdminBlogSettingsController {

    @Autowired
    private AdminBlogSettingsService adminBlogSettingsService;

    @PostMapping("/update")
    @ApiOperation(value = "更新设置接口")
    @ApiOperationLog(description = "更新设置接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO updateBlogSettingsReqVO){
        return adminBlogSettingsService.updateBlogSettings(updateBlogSettingsReqVO);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "获取详情")
    @ApiOperationLog(description = "获取详情")
    public Response findDetail(){
        return adminBlogSettingsService.findDetail();
    }
}
