package com.ayit.friend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.ayit.friend.enumeration.MessageType;
import com.ayit.friend.properties.ModuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyunOssUtil {
    @Value("${spring.cloud.alicloud.access-key}")//用一个单独配置类,放置这些属性.
    private String accessKey;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucketName;

    @Autowired
    private ModuleProperties moduleProperties;

    public String upload(InputStream inputStream,Integer fileType, String originalFilename) {
        String module = moduleProperties.getModule(fileType);

        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();

            String folder = localDate.format(formatter);

            //文件名：uuid.扩展名
            String fileName = UUID.randomUUID().toString();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String key = module + "/" + folder + "/" + fileName + fileExtension;

            //文件上传至阿里云
            ossClient.putObject(bucketName, key, inputStream);
            //返回url地址
            return "https://" + bucketName + "." + endpoint + "/" + key;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return null;
    }
}
