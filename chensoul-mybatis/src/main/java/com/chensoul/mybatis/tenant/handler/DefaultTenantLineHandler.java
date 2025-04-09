package com.chensoul.mybatis.tenant.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import com.chensoul.mybatis.tenant.util.TenantContextHolder;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
public class DefaultTenantLineHandler implements TenantLineHandler {

	private final Set<String> ignoredTables = new HashSet<>();

	public DefaultTenantLineHandler(Set<String> ignoredTables) {
		// 不同 DB 下，大小写的习惯不同，所以需要都添加进去
		ignoredTables.forEach(table -> {
			this.ignoredTables.add(table.toLowerCase());
			this.ignoredTables.add(table.toUpperCase());
		});
	}

	@Override
	public Expression getTenantId() {
		return new StringValue(TenantContextHolder.getTenantId());
	}

	@Override
	public boolean ignoreTable(String tableName) {
		return TenantContextHolder.isIgnored() // 情况一，全局忽略多租户
				|| ignoredTables.contains(SqlParserUtils.removeWrapperSymbol(tableName)); // 情况二，忽略多租户的表
	}

}
