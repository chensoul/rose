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

import com.chensoul.rose.core.util.StringPool;
import com.chensoul.rose.oss.model.BladeFile;
import com.chensoul.rose.oss.model.OssFile;
import com.chensoul.rose.oss.props.OssProperties;
import com.chensoul.rose.oss.rule.OssRule;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * QiniuTemplate
 *
 * @author Chill
 */
@AllArgsConstructor
public class QiniuTemplate implements OssTemplate {

    private final Auth auth;

    private final UploadManager uploadManager;

    private final BucketManager bucketManager;

    private final OssProperties ossProperties;

    private final OssRule ossRule;

    @Override
    @SneakyThrows
    public void makeBucket(String bucketName) {
        if (!ArrayUtils.contains(bucketManager.buckets(), getBucketName(bucketName))) {
            bucketManager.createBucket(
                    getBucketName(bucketName), Zone.autoZone().getRegion());
        }
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {}

    @Override
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return ArrayUtils.contains(bucketManager.buckets(), getBucketName(bucketName));
    }

    @Override
    @SneakyThrows
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    @Override
    @SneakyThrows
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    @SneakyThrows
    public OssFile statFile(String fileName) {
        return statFile(ossProperties.getBucketName(), fileName);
    }

    @Override
    @SneakyThrows
    public OssFile statFile(String bucketName, String fileName) {
        FileInfo stat = bucketManager.stat(getBucketName(bucketName), fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(StringUtils.isEmpty(stat.key) ? fileName : stat.key);
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(stat.hash);
        ossFile.setLength(stat.fsize);
        ossFile.setPutTime(new Date(stat.putTime / 10000));
        ossFile.setContentType(stat.mimeType);
        return ossFile;
    }

    @Override
    @SneakyThrows
    public String filePath(String fileName) {
        return getBucketName().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String filePath(String bucketName, String fileName) {
        return getBucketName(bucketName).concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String fileLink(String fileName) {
        return getEndpoint().concat(StringPool.SLASH).concat(fileName);
    }

    @Override
    @SneakyThrows
    public String fileLink(String bucketName, String fileName) {
        return getEndpoint().concat(StringPool.SLASH).concat(fileName);
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
            uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
        } else {
            Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
            int retry = 0;
            int retryCount = 5;
            while (response.needRetry() && retry < retryCount) {
                response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
                retry++;
            }
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setDomain(getOssHost());
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    @Override
    @SneakyThrows
    public void removeFile(String fileName) {
        bucketManager.delete(getBucketName(), fileName);
    }

    @Override
    @SneakyThrows
    public void removeFile(String bucketName, String fileName) {
        bucketManager.delete(getBucketName(bucketName), fileName);
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

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @return string
     */
    public String getUploadToken(String bucketName) {
        return auth.uploadToken(getBucketName(bucketName));
    }

    /**
     * 获取上传凭证，覆盖上传
     *
     * @param bucketName 存储桶名称
     * @param key        key
     * @return string
     */
    private String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(getBucketName(bucketName), key);
    }

    /**
     * 获取域名
     *
     * @return String
     */
    public String getOssHost() {
        return getEndpoint();
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
