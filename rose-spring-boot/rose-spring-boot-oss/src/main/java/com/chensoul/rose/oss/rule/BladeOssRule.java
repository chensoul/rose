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
package com.chensoul.rose.oss.rule;

import com.chensoul.rose.core.util.StringPool;
import com.chensoul.rose.core.util.date.DatePattern;
import com.chensoul.rose.core.util.date.TimeUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;

/**
 * 默认存储桶生成规则
 *
 * @author Chill
 */
@AllArgsConstructor
public class BladeOssRule implements OssRule {

    @Override
    public String bucketName(String bucketName) {
        return bucketName;
    }

    @Override
    public String fileName(String originalFilename) {
        return "upload" + StringPool.SLASH + TimeUtils.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN)
                + StringPool.SLASH + UUID.randomUUID() + StringPool.DOT + FilenameUtils.getExtension(originalFilename);
    }
}
