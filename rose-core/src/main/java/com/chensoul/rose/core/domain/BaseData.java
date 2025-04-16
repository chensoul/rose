package com.chensoul.rose.core.domain;

import static com.chensoul.rose.core.util.date.DatePattern.NORM_DATETIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @param <I>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseData<I extends Serializable> extends IdBased<I> implements Serializable, HasId<I> {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final long serialVersionUID = 5422817607129962637L;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    protected LocalDateTime createTime;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    protected LocalDateTime updateTime;

    public BaseData() {
        super();
    }

    public BaseData(I id) {
        super(id);
    }

    public BaseData(BaseData<I> data) {
        super(data.getId());
        this.createTime = data.getCreateTime();
        this.updateTime = data.getUpdateTime();
    }
}
