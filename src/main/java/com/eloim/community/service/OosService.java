package com.eloim.community.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.eloim.community.util.CommunityUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * oos上传服务
 */
@Service
public class OosService {

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Value("${spring.application.name}")
    private  String appName;

    public String uploadFile(MultipartFile file) throws IOException {


        InputStream inputStream = null;


            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

            // 获取上传文件的输入流
            inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();
            String uuid= CommunityUtil.generateUUID();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            fileName=appName+"/"+uuid+suffix;
            //调用oss实例中的方法实现上传
            //参数1： Bucket名称
            //参数2： 上传到oss文件路径和文件名称 /aa/bb/1.jpg
            //参数3： 上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://eloim-edu.oss-cn-hangzhou.aliyuncs.com/default.gif
            String url = "http://"+bucketName+"."+endpoint+"/"+fileName ;

            return url;

    }
}
