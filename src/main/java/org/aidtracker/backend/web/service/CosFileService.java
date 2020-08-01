package org.aidtracker.backend.web.service;

import org.aidtracker.backend.dao.CosFileRepository;
import org.aidtracker.backend.domain.CosFile;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.util.CosUtilFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 对象存储文件相关服务
 * @author mtage
 * @since 2020/7/30 16:33
 */
@Service
public class CosFileService {
    @Autowired
    private CosFileRepository cosFileRepository;

    @Value("${cos.qcloud.bucket-name}")
    private String qcloudBucketName;
    @Value("${cos.qcloud.secret-key}")
    private String qcloudSecretKey;
    @Value("${cos.qcloud.secret-id}")
    private String qcloudSecretId;
    @Value("${cos.qcloud.region}")
    private String qcloudRegion;
    @Value("${cos.qcloud.url-prefix}")
    private String qcloudUrlPrefix;

    /**
     * 文件上传至腾讯云对象存储
     * @param file
     * @param account
     * @return 结果文件数据
     */
    public CosFile uploadToQcloud(MultipartFile file, String comment, Account account) {
        // TODO 文件大小及上传数量限制
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "未选取文件");
        }
        try {
            CosUtilFactory.CosUtil cosUtil = CosUtilFactory.getInstance(qcloudSecretId, qcloudSecretKey,
                    qcloudBucketName, qcloudRegion);
            String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
            File tempFile = File.createTempFile(baseName, "." + FilenameUtils.getExtension(file.getOriginalFilename()));
            file.transferTo(tempFile);
            String key = cosUtil.uploadFile(tempFile);
            CosFile cosFile = new CosFile();
            cosFile.setName(key);
            cosFile.setAccountId(account.getAccountId());
            cosFile.setUrl(qcloudUrlPrefix + key);
            cosFile.setComment(comment);
            cosFile = cosFileRepository.save(cosFile);
            return cosFile;
        } catch (IOException e) {
            throw new CommonSysException(AidTrackerCommonErrorCode.COS_ERROR.getErrorCode(), "连接至腾讯云对象存储失败", e);
        }
    }

}
