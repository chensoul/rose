package com.chensoul.spring.boot.oss.rule;

import com.chensoul.core.util.StringPool;
import com.chensoul.core.util.date.DatePattern;
import com.chensoul.core.util.date.TimeUtils;
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
