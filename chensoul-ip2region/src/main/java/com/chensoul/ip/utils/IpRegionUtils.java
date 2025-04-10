package com.chensoul.ip.utils;

import com.chensoul.ip.Area;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.StreamUtils;

/**
 * IP 工具类
 * <p>
 * IP 数据源来自 ip2region.xdb 精简版，基于 <a href="https://gitee.com/zhijiantianya/ip2region"/> 项目
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Slf4j
public class IpRegionUtils {

    /**
     * IP 查询器，启动加载到内存中
     */
    private static Searcher SEARCHER;

    static {
        try {
            long now = System.currentTimeMillis();
            ClassLoader classLoader = (ClassLoader) ObjectUtils.firstNonNull(
                    Thread.currentThread().getContextClassLoader(), IpRegionUtils.class.getClassLoader());
            byte[] bytes = StreamUtils.copyToByteArray(
                    classLoader.getResource("ip2region.xdb").openStream());
            SEARCHER = Searcher.newWithBuffer(bytes);
            log.info("启动加载 IpRegionUtils 成功，耗时 ({}) 毫秒", System.currentTimeMillis() - now);
        } catch (IOException e) {
            log.error("启动加载 IpRegionUtils 失败", e);
        }
    }

    /**
     * 查询 IP 对应的地区编号
     *
     * @param ip IP 地址，格式为 127.0.0.1
     * @return 地区id
     */
    @SneakyThrows
    public static Integer getAreaId(String ip) {
        return Integer.parseInt(SEARCHER.searchByStr(ip.trim()));
    }

    /**
     * 查询 IP 对应的地区编号
     *
     * @param ip IP 地址的时间戳，格式参考{@link Searcher#checkIpAddr(String)} 的返回
     * @return 地区编号
     */
    @SneakyThrows
    public static Integer getAreaId(long ip) {
        return Integer.parseInt(SEARCHER.search(ip));
    }

    /**
     * 查询 IP 对应的地区
     *
     * @param ip IP 地址，格式为 127.0.0.1
     * @return 地区
     */
    public static Area getArea(String ip) {
        return AreaUtils.getArea(getAreaId(ip));
    }

    /**
     * 查询 IP 对应的地区
     *
     * @param ip IP 地址的时间戳，格式参考{@link Searcher#checkIpAddr(String)} 的返回
     * @return 地区
     */
    public static Area getArea(long ip) {
        return AreaUtils.getArea(getAreaId(ip));
    }
}
