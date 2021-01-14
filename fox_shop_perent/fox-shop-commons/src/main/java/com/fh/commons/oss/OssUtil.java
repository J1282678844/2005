package com.fh.commons.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName OssUtil
 * @Deacription TODO
 * @Author 阳
 * @Date 2020/12/3 11:35
 * @Version 1.0
 **/
public class OssUtil {

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    public static final String accessKeyId = "LTAI4G5LWyrAWMUgUioo8CKg";
    public static final String accessKeySecret = "uudfX0QQQsGPTbzwgJPL32ekrvpWJp";
    //访问地址
    public static final String fileUrl = "https://2-0-0-5.oss-cn-beijing.aliyuncs.com/";
    public static final String bucketName = "2-0-0-5";

    public static String uploadFile(String mkdir, MultipartFile file) throws IOException {
        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String substring = fileName.substring(fileName.lastIndexOf("."));
        //拼接新名称
        String newFileName = UUID.randomUUID().toString() + substring;
        //上传路径
        String path = mkdir + "/" + newFileName;
        // 创建OSSClient实例--->连接OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件--->到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        ossClient.putObject(bucketName, path, file.getInputStream());
        // 关闭OSSClient。
        ossClient.shutdown();
        return fileUrl + "/" + path;
    }
}
