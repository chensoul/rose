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
package com.chensoul.rose.oss.old.storage;

import com.chensoul.rose.oss.old.storage.domain.DownloadResponse;
import com.chensoul.rose.oss.old.storage.domain.StorageItem;
import com.chensoul.rose.oss.old.storage.domain.StorageRequest;
import com.chensoul.rose.oss.old.storage.domain.StorageResponse;
import com.chensoul.rose.oss.old.storage.exception.StorageException;
import com.chensoul.rose.oss.old.storage.properties.BaseOssProperties;
import com.chensoul.rose.oss.old.storage.properties.MinioOssProperties;
import com.google.common.collect.Maps;
import io.minio.*;
import io.minio.messages.Item;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

/**
 * @author Levin
 */
@Slf4j
@AllArgsConstructor
public class MinioOssOperation implements OssOperation {

    private final MinioClient minioClient;

    private final MinioOssProperties properties;

    @Override
    public DownloadResponse download(String fileName) {
        return download(properties.getBucket(), fileName);
    }

    @Override
    public DownloadResponse download(String bucketName, String fileName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return DownloadResponse.builder().inputStream(inputStream).build();
        } catch (Exception ex) {
            log.error("[文件下载异常]", ex);
            throw downloadError(BaseOssProperties.StorageType.MINIO, ex);
        }
    }

    @Override
    public void download(String bucketName, String fileName, File file) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("[文件下载异常]", e);
        }
    }

    @Override
    public void download(String fileName, File file) {
        download(properties.getBucket(), fileName, file);
    }

    /**
     * 对象转换
     *
     * @param iterable 迭代内容
     * @return 返回结果
     */
    private List<StorageItem> getStorageItems(Iterable<Result<Item>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true)
                .map(itemResult -> {
                    try {
                        StorageItem storageItem = new StorageItem();
                        Item item = itemResult.get();
                        storageItem.setName(item.objectName());
                        storageItem.setSize(item.size());
                        Map<String, Object> extended = Maps.newHashMap();
                        extended.put("tag", item.etag());
                        extended.put("storageClass", item.storageClass());
                        extended.put("lastModified", item.lastModified());
                        storageItem.setExtended(extended);
                        return storageItem;
                    } catch (Exception e) {
                        throw new StorageException(
                                BaseOssProperties.StorageType.MINIO, "Error while parsing list of objects", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StorageItem> list() {
        Iterable<Result<Item>> iterable = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(properties.getBucket())
                .region(properties.getRegion())
                .build());
        return getStorageItems(iterable);
    }

    @Override
    public void rename(String oldName, String newName) {
        throw new StorageException(BaseOssProperties.StorageType.MINIO, "方法未实现");
    }

    @Override
    public void rename(String bucketName, String oldName, String newName) {
        throw new StorageException(BaseOssProperties.StorageType.MINIO, "方法未实现");
    }

    @Override
    public StorageResponse upload(String fileName, byte[] content) {
        InputStream stream = new ByteArrayInputStream(content);
        return upload(properties.getBucket(), fileName, stream);
    }

    @Override
    public StorageResponse upload(String bucketName, String fileName, InputStream content) {
        return upload(StorageRequest.builder()
                .bucket(bucketName)
                .originName(fileName)
                .inputStream(content)
                .build());
    }

    @Override
    public StorageResponse upload(String bucketName, String fileName, byte[] content) {
        return upload(bucketName, fileName, new ByteArrayInputStream(content));
    }

    @Override
    public StorageResponse upload(StorageRequest request) {
        try {
            String bucket = StringUtils.defaultString(request.getBucket(), properties.getBucket());
            String fileName = request.isRandomName() ? request.buildTargetName() : request.getOriginName();
            final InputStream inputStream = request.getInputStream();
            final ObjectWriteResponse object =
                    minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileName).stream(
                                    inputStream, inputStream.available(), -1)
                            .contentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType())
                            .build());
            return StorageResponse.builder()
                    .etag(object.etag())
                    .originName(request.getOriginName())
                    .targetName(fileName)
                    .fullUrl(properties.getMappingPath() + fileName)
                    .build();
        } catch (Exception e) {
            log.error("[文件上传失败]", e);
            throw new StorageException(BaseOssProperties.StorageType.MINIO, "文件上传失败," + e.getLocalizedMessage());
        }
    }

    @Override
    public void remove(String fileName) {
        remove(properties.getBucket(), fileName);
    }

    @Override
    public void remove(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            log.error("[文件删除失败]", e);
        }
    }

    @Override
    public void remove(String bucketName, Path path) {
        remove(bucketName, path.toString());
    }
}
