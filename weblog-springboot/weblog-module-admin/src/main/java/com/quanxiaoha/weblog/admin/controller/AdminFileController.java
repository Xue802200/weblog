package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.service.AdminFileService;
import com.quanxiaoha.weblog.common.aspect.ApiOperationLog;
import com.quanxiaoha.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 文件模块")
public class AdminFileController {

    @Autowired
    private AdminFileService adminFileService;

    @PostMapping("/file/upload")
    @ApiOperation(value = "文件上传接口")
    @ApiOperationLog(description = "文件上传接口")
    public Response fileUpload(@RequestParam("file") MultipartFile file) {
        return adminFileService.uploadFile(file);
    }
}
