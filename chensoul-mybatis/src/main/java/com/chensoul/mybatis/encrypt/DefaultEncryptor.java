package com.chensoul.mybatis.encrypt;

import com.chensoul.mybatis.encrypt.util.Algorithm;
import com.chensoul.mybatis.encrypt.util.AlgorithmUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultEncryptor implements IEncryptor {

	public String encrypt(Algorithm algorithm, String password, String plaintext, Object metaObject) {
		return AlgorithmUtils.resolve(algorithm, password, true, plaintext);
	}

	public String decrypt(Algorithm algorithm, String password, String plaintext, Object metaObject) {
		return AlgorithmUtils.resolve(algorithm, password, false, plaintext);
	}

}
