package com.chensoul.mybatis.tenant.handler;

import com.chensoul.mybatis.extension.interceptor.DefaultMetaObjectHandler;
import com.chensoul.mybatis.tenant.util.TenantContextHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class TenantMetaObjectHandler extends DefaultMetaObjectHandler {

	private final String tenantFiledName;

	@Override
	public void insertFill(MetaObject metaObject) {
		super.insertFill(metaObject);
		fillValIfNullByName(tenantFiledName, TenantContextHolder.getTenantId(), metaObject, false);
	}

}
