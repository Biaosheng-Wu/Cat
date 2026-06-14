package org.example.catcarebusiness.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 通用图片上传接口
     * 前端通过 form-data 格式提交名为 "file" 的图片文件
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败：文件不能为空！";
        }

        OSS ossClient = null;
        try {
            // 1. 获取上传文件的原始输入流
            InputStream inputStream = file.getInputStream();

            // 2. 自动生成一个全网唯一的文件名，防止多人上传同名图片时发生覆盖 (例如: uuid.jpg)
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "cats/" + UUID.randomUUID().toString() + suffix;

            // 3. 初始化阿里云 OSS 客户端
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 4. 将文件流推送到阿里云指定的存储桶中
            ossClient.putObject(bucketName, fileName, inputStream);

            // 5. 拼装出该图片在互联网上的公开访问绝对路径
            // 格式类似：https://cat-business.oss-cn-beijing.aliyuncs.com/cats/xxxx.jpg
            String fileUrl = "https://" + bucketName + "." + endpoint + "/" + fileName;

            // 6. 将这个极其宝贵的网址返回给前端
            return fileUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败，服务器发生异常：" + e.getMessage();
        } finally {
            if (ossClient != null) {
                // 极重要：用完记得关闭客户端，释放连接资源
                ossClient.shutdown();
            }
        }
    }
}