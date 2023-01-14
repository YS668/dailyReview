package com.back.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.*;

public class AliOssUtil {

    /**
     * 访问外网域名
     */
    private static String endpoint = "oss-cn-guangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI5tF46nBCkA5f8YfyCGLy";
    private static String accessKeySecret = "RuBBUlIbZgOANg86a10JWRmXdphMwK";
    private static String bucketName = "dr-data";
    private static String keySuffixWithSlash = "back/";;

    /**
     * 上传工具类
     * @param fileName
     *        上传的路径
     * @param saveFileName
     *        保存oss的路径
     * @return
     * @throws IOException
     */
    public static void upload(String fileName,String saveFileName) throws IOException {
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        client.putObject(bucketName, keySuffixWithSlash+saveFileName, new FileInputStream(fileName));
        client.shutdown();
    }

    /**
     * 下载文件
     * @param fileName
     */
    public static void download(String fileName){
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        OSSObject object = client.getObject(bucketName, keySuffixWithSlash + fileName);
        client.shutdown();
    }

}
