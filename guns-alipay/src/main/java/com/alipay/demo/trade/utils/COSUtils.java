package com.alipay.demo.trade.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;

public class COSUtils {

    public static String qrCode(File file){

        String name = file.getName();
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDoRoxqTp52lPuEKvLMPCmAIAoLhvxKHZc";
        String secretKey = "1pOineIkxvxoeQ1MTLyKRIxLSKwOZtyn";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        String key="";
        try {
            // 指定要上传的文件
            //File localFile = new File("exampleobject");
            // 指定要上传到的存储桶
            String bucketName = "picture-agang-1300811584";
            // 指定要上传到 COS 上对象键
            key = "qrCodeImge/"+name;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return key;
    }
}
