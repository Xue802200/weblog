package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.atricle.*;
import com.quanxiaoha.weblog.admin.service.AdminArticleService;
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
@RequestMapping("/admin/article")
@Api(tags = "Admin 文章模块")
public class AdminArticleController {

    @Autowired
    private AdminArticleService adminArticleService;

    @PostMapping("/publish")
    @ApiOperation(value = "发布文章接口")
    @ApiOperationLog(description = "发布文章接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response publishArticle(@RequestBody @Validated PublishArticleReqVO publishArticleReqVO){
        return adminArticleService.publishArticle(publishArticleReqVO);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除文章接口")
    @ApiOperationLog(description = "删除文章接口")
    public Response deleteArticle(@RequestBody @Validated DeleteArticleReqVO deleteArticleReqVO){
        return adminArticleService.deleteArticle(deleteArticleReqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询接口")
    @ApiOperationLog(description = "分页查询接口")
    public Response findArticleList(@RequestBody @Validated FindArticlePageListReqVO findArticlePageListReqVO){
        return adminArticleService.findArticleList(findArticlePageListReqVO);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "获取文章详情接口")
    @ApiOperationLog(description = "获取文章详情接口")
    public Response findArticleDetail(@RequestBody @Validated FindArticleDetailReqVO findArticleDetailReqVO){
        return adminArticleService.findArticleDetail(findArticleDetailReqVO);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新文章接口")
    @ApiOperationLog(description = "更新文章接口")
    public Response updateArticle(@RequestBody @Validated UpdateArticleReqVO updateArticleReqVO){
        return adminArticleService.updateArticle(updateArticleReqVO);
    }
}
