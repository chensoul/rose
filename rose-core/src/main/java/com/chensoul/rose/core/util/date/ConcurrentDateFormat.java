package com.chensoul.rose.core.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.SneakyThrows;

/**
 * 参考tomcat8中的并发DateFormat
 * <p>
 * {@link SimpleDateFormat}的线程安全包装器。 不使用ThreadLocal，创建足够的SimpleDateFormat对象来满足并发性要求。
 */
public class ConcurrentDateFormat {

    private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

    private final String format;

    private final Locale locale;

    private final TimeZone timezone;

    private ConcurrentDateFormat(String format, Locale locale, TimeZone timezone) {
        this.format = format;
        this.locale = locale;
        this.timezone = timezone;
        queue.add(createInstance());
    }

    public static ConcurrentDateFormat of(String format) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
    }

    public static ConcurrentDateFormat of(String format, TimeZone timezone) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), timezone);
    }

    public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timezone) {
        return new ConcurrentDateFormat(format, locale, timezone);
    }

    public String format(Date date) {
        SimpleDateFormat sdf = queue.poll();
        if (sdf == null) {
            sdf = createInstance();
        }
        String result = sdf.format(date);
        queue.add(sdf);
        return result;
    }

    @SneakyThrows
    public Date parse(String source) {
        SimpleDateFormat sdf = queue.poll();
        if (sdf == null) {
            sdf = createInstance();
        }
        Date result = sdf.parse(source);
        queue.add(sdf);
        return result;
    }

    private SimpleDateFormat createInstance() {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setTimeZone(timezone);
        return sdf;
    }
}
