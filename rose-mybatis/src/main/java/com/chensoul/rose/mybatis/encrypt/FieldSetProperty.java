package com.chensoul.rose.mybatis.encrypt;

import com.chensoul.rose.mybatis.encrypt.annotation.FieldBind;
import com.chensoul.rose.mybatis.encrypt.annotation.FieldEncrypt;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class FieldSetProperty {

    private String fieldName;

    private FieldEncrypt fieldEncrypt;

    private FieldBind fieldBind;
}
