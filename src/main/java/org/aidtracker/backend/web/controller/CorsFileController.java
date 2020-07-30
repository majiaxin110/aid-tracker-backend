package org.aidtracker.backend.web.controller;

import io.swagger.annotations.Api;
import org.aidtracker.backend.domain.CorsFile;
import org.aidtracker.backend.util.GlobalAuthUtil;
import org.aidtracker.backend.util.SimpleResult;
import org.aidtracker.backend.web.service.CorsFileService;
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
public class CorsFileController {
    @Autowired
    CorsFileService corsFileService;

    @PostMapping("/cors-file")
    public SimpleResult<CorsFile> upload(MultipartFile file, @RequestParam String comment) {
        return SimpleResult.success(corsFileService.uploadToQcloud(file, comment, GlobalAuthUtil.authedAccount()));
    }
}
