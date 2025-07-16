package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.quanxiaoha.weblog.admin.service.AdminCategoryService;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @PostMapping("/category/add")
    @ApiOperation(value = "添加分类")
    @ApiOperationLog(description = "添加分类")
    public Response addCategory(@RequestBody  @Validated AddCategoryReqVO  addCategoryReqVO) {
        return adminCategoryService.addCategory(addCategoryReqVO);
    }
}
