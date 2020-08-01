package org.aidtracker.backend.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.Getter;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 对象存储相关 目前仅支持腾讯云
 *
 * @author mtage
 * @since 2020/1/27 18:24
 */
public class CosUtilFactory {
    private static CosUtil cosUtil;

    /**
     * 获取对象存储工具实例
     *
     * @param secretId
     * @param secretKey
     * @param bucketName
     * @return
     * @throws IOException 存储桶未找到
     */
    public static synchronized CosUtil getInstance(String secretId, String secretKey, String bucketName, String regionName) throws IOException {
        if (Objects.isNull(cosUtil)) {
            cosUtil = new CosUtil(secretId, secretKey, bucketName, regionName);
        }
        return cosUtil;
    }

    public static class CosUtil {
        @Getter
        private String secretId;
        @Getter
        private String secretKey;
        @Getter
        private String bucketName;
        @Getter
        private String regionName;
        /**
         * cos客户端
         */
        public COSClient cosClient;

        public Bucket goalBucket;

        public CosUtil(String secretId, String secretKey, String bucketName, String regionName) throws IOException {
            this.secretId = secretId;
            this.secretKey = secretKey;
            this.bucketName = bucketName;
            this.regionName = regionName;
            init();
        }

        private CosUtil() {
        }

        public void init() throws IOException {
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            Region region = new Region(this.regionName);
            ClientConfig clientConfig = new ClientConfig(region);
            clientConfig.setHttpProtocol(HttpProtocol.https);

            cosClient = new COSClient(cred, clientConfig);

            List<Bucket> allBucket = cosClient.listBuckets();
            goalBucket = allBucket.stream().filter(eachBucket -> eachBucket.getName().equals(bucketName))
                    .findFirst().orElseThrow(() -> new IOException("无目标存储桶 " + bucketName));
        }

        /**
         * 上传文件 将根据文件名和当前时间自动生成键
         *
         * @param file
         * @return 文件键值
         * @throws CosClientException
         */
        public String uploadFile(File file) {
            // 获得存储桶
            String[] nameSplits = file.getName().split("\\.");
            String key = file.getName().substring(0, file.getName().lastIndexOf(".")) + "-" + new DateTime().toString("yyyy_MM_dd") +
                    "." + nameSplits[nameSplits.length - 1];
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return key;
        }

        /**
         * 指定键值删除文件
         *
         * @param key
         * @throws CosClientException
         */
        public void deleteFile(String key) {
            cosClient.deleteObject(bucketName, key);
        }

        public void shutdown() {
            cosClient.shutdown();
        }
    }
}
