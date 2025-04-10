package com.chensoul.core.jackson;

import com.chensoul.core.util.date.DatePattern;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public class Java8TimeModule extends SimpleModule {

    public Java8TimeModule() {
        super(PackageVersion.VERSION);

        // ======================= 时间序列化规则 ===============================
        // yyyy-MM-dd HH:mm:ss
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
        // yyyy-MM-dd
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // HH:mm:ss
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        // Instant 类型序列化
        this.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        // Duration 类型序列化
        this.addSerializer(Duration.class, DurationSerializer.INSTANCE);

        // ======================= 时间反序列化规则 ==============================
        // yyyy-MM-dd HH:mm:ss
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        // yyyy-MM-dd
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // HH:mm:ss
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        // Instant 反序列化
        this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        // Duration 反序列化
        this.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
    }
}
