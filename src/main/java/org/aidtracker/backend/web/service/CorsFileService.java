package org.aidtracker.backend.web.service;

import lombok.Setter;
import org.aidtracker.backend.dao.CorsFileRepository;
import org.aidtracker.backend.domain.CorsFile;
import org.aidtracker.backend.domain.account.Account;
import org.aidtracker.backend.util.AidTrackerCommonErrorCode;
import org.aidtracker.backend.util.CommonSysException;
import org.aidtracker.backend.util.CorsUtilFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
public class CorsFileService {
    @Autowired
    private CorsFileRepository corsFileRepository;

    @Value("${cors.qcloud.bucket-name}")
    private String qcloudBucketName;
    @Value("${cors.qcloud.secret-key}")
    private String qcloudSecretKey;
    @Value("${cors.qcloud.secret-id}")
    private String qcloudSecretId;
    @Value("${cors.qcloud.region}")
    private String qcloudRegion;
    @Value("${cors.qcloud.url-prefix}")
    private String qcloudUrlPrefix;

    /**
     * 文件上传至腾讯云对象存储
     * @param file
     * @param account
     * @return 结果文件数据
     */
    public CorsFile uploadToQcloud(MultipartFile file, String comment, Account account) {
        // TODO 文件大小及上传数量限制
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new CommonSysException(AidTrackerCommonErrorCode.INVALID_PARAM.getErrorCode(), "未选取文件");
        }
        try {
            CorsUtilFactory.CorsUtil corsUtil = CorsUtilFactory.getInstance(qcloudSecretId, qcloudSecretKey,
                    qcloudBucketName, qcloudRegion);
            String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
            File tempFile = File.createTempFile(baseName, "." + FilenameUtils.getExtension(file.getOriginalFilename()));
            file.transferTo(tempFile);
            String key = corsUtil.uploadFile(tempFile);
            CorsFile corsFile = new CorsFile();
            corsFile.setName(key);
            corsFile.setAccountId(account.getAccountId());
            corsFile.setUrl(qcloudUrlPrefix + key);
            corsFile.setComment(comment);
            corsFile = corsFileRepository.save(corsFile);
            return corsFile;
        } catch (IOException e) {
            throw new CommonSysException(AidTrackerCommonErrorCode.CORS_ERROR.getErrorCode(), "连接至腾讯云对象存储失败", e);
        }
    }

}
