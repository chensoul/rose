package com.chensoul.excel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@Accessors(chain = true)
public class ImportResultDTO {

    private Integer success;

    private Integer fail;

    private String failUrl;
}
