package com.chensoul.rose.mybatis.encrypt;

import com.chensoul.rose.mybatis.encrypt.util.Algorithm;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface IEncryptor {

    String encrypt(Algorithm algorithm, String password, String plaintext, Object metaObject);

    String decrypt(Algorithm algorithm, String password, String plaintext, Object metaObject);
}
