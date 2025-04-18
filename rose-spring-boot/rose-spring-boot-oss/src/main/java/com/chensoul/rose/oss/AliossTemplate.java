/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.chensoul.rose.core.jackson.JacksonUtils;
import com.chensoul.rose.core.util.StringPool;
import com.chensoul.rose.oss.model.BladeFile;
import com.chensoul.rose.oss.model.OssFile;
import com.chensoul.rose.oss.props.OssProperties;
import com.chensoul.rose.oss.rule.OssRule;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * AliossTemplate
 *
 * @author Chill
 */
@AllArgsConstructor
public class AliossTemplate implements OssTemplate {

    private final OSSClient ossClient;

    private final OssProperties ossProperties;

    private final OssRule ossRule;

    @Override
    @SneakyThrows
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            ossClient.createBucket(getBucketName(bucketName));
        }
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        ossClient.deleteBucket(getBucketName(bucketName));
    }

    @Override
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(getBucketName(bucketName));
    }

    @Override
    @SneakyThrows
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    @Override
    @SneakyThrows
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    @SneakyThrows
    public OssFile statFile(String fileName) {
        return statFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    @SneakyThrows
    public OssFile statFile(String bucketName, String fileName) {
        ObjectMetadata stat = ossClient.getObjectMetadata(getBucketName(bucketName), fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(fileName);
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(stat.getContentMD5());
        ossFile.setLength(stat.getContentLength());
        ossFile.setPutTime(stat.getLastModified());
        ossFile.setContentType(stat.getContentType());
        return ossFile;
    }

    @Override
    @SneakyThrows
    public String filePath(String fileName) {
        return getOssHost().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String filePath(String bucketName, String fileName) {
        return getOssHost(bucketName).concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String fileLink(String fileName) {
        return getOssHost().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String fileLink(String bucketName, String fileName) {
        return getOssHost(bucketName).concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public BladeFile putFile(MultipartFile file) {
        return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
    }

    @Override
    @SneakyThrows
    public BladeFile putFile(String fileName, MultipartFile file) {
        return putFile(ossProperties.getBucketName(), fileName, file);
    }

    @Override
    @SneakyThrows
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file) {
        return putFile(bucketName, fileName, file.getInputStream());
    }

    @Override
    @SneakyThrows
    public BladeFile putFile(String fileName, InputStream stream) {
        return putFile(ossProperties.getBucketName(), fileName, stream);
    }

    @Override
    @SneakyThrows
    public BladeFile putFile(String bucketName, String fileName, InputStream stream) {
        return put(bucketName, stream, fileName, false);
    }

    @SneakyThrows
    public BladeFile put(String bucketName, InputStream stream, String key, boolean cover) {
        makeBucket(bucketName);
        String originalName = key;
        key = getFileName(key);
        // 覆盖上传
        if (cover) {
            ossClient.putObject(getBucketName(bucketName), key, stream);
        } else {
            PutObjectResult response = ossClient.putObject(getBucketName(bucketName), key, stream);
            int retry = 0;
            int retryCount = 5;
            while (StringUtils.isEmpty(response.getETag()) && retry < retryCount) {
                response = ossClient.putObject(getBucketName(bucketName), key, stream);
                retry++;
            }
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setDomain(getOssHost(bucketName));
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    @Override
    @SneakyThrows
    public void removeFile(String fileName) {
        ossClient.deleteObject(getBucketName(), fileName);
    }

    @Override
    @SneakyThrows
    public void removeFile(String bucketName, String fileName) {
        ossClient.deleteObject(getBucketName(bucketName), fileName);
    }

    @Override
    @SneakyThrows
    public void removeFiles(List<String> fileNames) {
        fileNames.forEach(this::removeFile);
    }

    @Override
    @SneakyThrows
    public void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @return String
     */
    private String getBucketName() {
        return getBucketName(ossProperties.getBucketName());
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    private String getBucketName(String bucketName) {
        return ossRule.bucketName(bucketName);
    }

    /**
     * 根据规则生成文件名称规则
     *
     * @param originalFilename 原始文件名
     * @return string
     */
    private String getFileName(String originalFilename) {
        return ossRule.fileName(originalFilename);
    }

    public String getUploadToken() {
        return getUploadToken(ossProperties.getBucketName());
    }

    /**
     * TODO 过期时间
     * <p>
     * 获取上传凭证，普通上传
     */
    public String getUploadToken(String bucketName) {
        // 默认过期时间2小时
        return getUploadToken(bucketName, (Long) ossProperties.getArgs().getOrDefault("expireTime", 3600L));
    }

    public String getUploadToken(String bucketName, long expireTime) {
        String baseDir = "upload";

        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policyConds = new PolicyConditions();
        // 默认大小限制10M
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, (Long)
                ossProperties.getArgs().getOrDefault("contentLengthRange", 10485760L));
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, baseDir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> respMap = new LinkedHashMap<>(16);
        respMap.put("accessid", ossProperties.getAccessKey());
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", baseDir);
        respMap.put("host", getOssHost(bucketName));
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        return JacksonUtils.toString(respMap);
    }

    /**
     * 获取域名
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    public String getOssHost(String bucketName) {
        String prefix = getEndpoint().contains("https://") ? "https://" : "http://";
        return prefix
                + getBucketName(bucketName)
                + StringPool.DOT
                + getEndpoint().replaceFirst(prefix, StringPool.EMPTY);
    }

    /**
     * 获取域名
     *
     * @return String
     */
    public String getOssHost() {
        return getOssHost(ossProperties.getBucketName());
    }

    /**
     * 获取服务地址
     *
     * @return String
     */
    public String getEndpoint() {
        if (StringUtils.isBlank(ossProperties.getTransformEndpoint())) {
            return ossProperties.getEndpoint();
        }
        return ossProperties.getTransformEndpoint();
    }
}
