package com.weiyun;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;

/**
 * @author yangchangjian
 */
public class COSTest {
    public static void main(String[] args) {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials("accessKey",
                "secretKey");
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region("ap-beijing"));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = "bucketName";
        if (cosclient.doesObjectExist(bucketName, "ada/data23.jpg")) {
            System.out.println("對象在");
        } else {
            System.out.println("對象不存在");
        }

    }

    /**
     * 创建存储桶
     *
     * @param cosclient  cosclient
     * @param bucketName 存储桶的名字
     */
    public static void createBucket(COSClient cosclient, String bucketName) {
        try {
            if (cosclient.doesBucketExist(bucketName)) {
                System.out.println("該存儲桶以存在");
            } else {
                Bucket bucket = cosclient.createBucket(bucketName);
                System.out.println("创建成功");
            }
        } catch (Exception e) {
            System.out.println(bucketName + "創建異常，" + e.toString());
        }
    }

    /**
     * 刪除存儲桶
     *
     * @param cosClient  cosclient
     * @param bucketName 存储桶的名字
     */
    public static void delBucket(COSClient cosClient, String bucketName) {
        try {
            if (cosClient.doesBucketExist(bucketName)) {
                cosClient.deleteBucket(bucketName);
                System.out.println("刪除成功" + bucketName);
            }
        } catch (Exception e) {
            System.out.println("刪除失敗" + bucketName + ",異常情況為：" + e.toString());
        }
    }

    /**
     * 获取bucket的Region
     *
     * @param cosClient
     * @param bucketName
     * @return
     */
    public static String getBucketLoction(COSClient cosClient, String bucketName) {
        try {
            if (cosClient.doesBucketExist(bucketName)) {
                String location = cosClient.getBucketLocation(bucketName);
                System.out.println(bucketName + "的region是" + location);
                return location;
            } else {
                System.out.println("該存儲桶不存在");
                return "fial";
            }
        } catch (Exception e) {
            System.out.println("獲取失敗");
        }
        return "fail";
    }

    public static ObjectListing getListObjects(COSClient cosClient, String bucketName) {
        try {
            if (cosClient.doesBucketExist(bucketName)) {
                ObjectListing listObjects = cosClient.listObjects(bucketName);
                System.out.println(listObjects.toString());
                return listObjects;
            } else {
                System.out.println("該存儲桶不存在");
            }
        } catch (Exception e) {
            System.out.println("獲取失敗");
        }
        return null;
    }

    /**
     * 上傳文件
     *
     * @param cosClient  cosclient
     * @param bucketName bucket名字
     * @param key        文件別名
     * @param localFile  本地文件file
     */
    public static void putObject(COSClient cosClient, String bucketName, String key, File localFile) {
        try {
            if (cosClient.doesBucketExist(bucketName)) {
                PutObjectResult result = cosClient.putObject(bucketName, key, localFile);
                System.out.println(result.getETag());
            } else {
                System.out.println("该存储桶不存在");
            }
        } catch (Exception e) {
            System.out.println("发生异常" + e.toString());
        }
    }

    public static void deleteObj(COSClient cosClient, String bucketName, String key) {
        try {
            if (cosClient.doesBucketExist(bucketName)) {
                cosClient.deleteObject(bucketName, key);
            } else {
                System.out.println("存储桶不存在");
            }
        } catch (Exception e) {
            System.out.println("异常");
        }
    }

    /**
     * 下载存储的对象到本地文件
     *
     * @param cosClient  cosClient
     * @param bucketName bucketName
     * @param key        相對文件名
     * @param downFile   下载的位置
     */
    public static void downloadFile(COSClient cosClient, String bucketName, String key, File downFile) {
        // 方法 2 下载文件到本地
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }
}