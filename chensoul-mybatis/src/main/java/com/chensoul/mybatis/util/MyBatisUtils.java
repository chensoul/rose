package com.chensoul.mybatis.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.chensoul.core.domain.ToData;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.*;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MyBatisUtils {

	private static final String MYSQL_ESCAPE_CHARACTER = "`";

	public static <T> T getData(ToData<T> data) {
		T object = null;
		if (data != null) {
			object = data.toData();
		}
		return object;
	}

	public static <T> T getData(Optional<? extends ToData<T>> data) {
		T object = null;
		if (data.isPresent()) {
			object = data.get().toData();
		}
		return object;
	}

	public static <T> List<T> convertDataList(Collection<? extends ToData<T>> toDataList) {
		List<T> list = Collections.emptyList();
		if (toDataList != null && !toDataList.isEmpty()) {
			list = new ArrayList<>();
			for (ToData<T> object : toDataList) {
				if (object != null) {
					list.add(object.toData());
				}
			}
		}
		return list;
	}

	/**
	 * 将拦截器添加到链中 由于 MybatisPlusInterceptor 不支持添加拦截器，所以只能全量设置
	 *
	 * @param interceptor 链
	 * @param inner       拦截器
	 * @param index       位置
	 */
	public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
		List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
		inners.add(index, inner);
		interceptor.setInterceptors(inners);
	}

	/**
	 * 获得 Table 对应的表名
	 * <p>
	 * 兼容 MySQL 转义表名 `t_xxx`
	 *
	 * @param table 表
	 * @return 去除转移字符后的表名
	 */
	public static String getTableName(Table table) {
		String tableName = table.getName();
		if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
			tableName = tableName.substring(1, tableName.length() - 1);
		}
		return tableName;
	}

	/**
	 * 构建 Column 对象
	 *
	 * @param tableName  表名
	 * @param tableAlias 别名
	 * @param column     字段名
	 * @return Column 对象
	 */
	public static Column buildColumn(String tableName, Alias tableAlias, String column) {
		if (tableAlias != null) {
			tableName = tableAlias.getName();
		}
		return new Column(tableName + StringPool.DOT + column);
	}

}
