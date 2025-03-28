package com.chensoul.spring.boot.mybatis.cosid;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import me.ahoo.cosid.provider.DefaultIdGeneratorProvider;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class CosidIdentifierGenerator implements IdentifierGenerator {
	@Override
	public Number nextId(Object entity) {
		return DefaultIdGeneratorProvider.INSTANCE.getShare().generate();
	}
}
