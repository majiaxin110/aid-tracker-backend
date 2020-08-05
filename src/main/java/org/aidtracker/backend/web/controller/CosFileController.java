package org.aidtracker.backend.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aidtracker.backend.domain.CosFile;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.service.CosFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mtage
 * @since 2020/7/30 16:58
 */
@RestController
@Api("简单文件上传")
public class CosFileController {
    @Autowired
    CosFileService cosFileService;

    @PostMapping("/cos-file")
    @ApiOperation("文件上传")
    public SimpleResult<CosFile> upload(MultipartFile file, @RequestParam(required = false) String comment) {
        return SimpleResult.success(cosFileService.uploadToQcloud(file, comment, GlobalAuthUtil.authedAccount()));
    }
}
