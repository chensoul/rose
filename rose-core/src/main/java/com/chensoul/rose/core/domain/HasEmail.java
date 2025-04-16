package com.chensoul.rose.core.domain;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface HasEmail extends HasName {

    String EMAIL_REGEXP = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    String getEmail();
}
