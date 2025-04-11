package com.chensoul.core.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdBased<I extends Serializable> implements HasId<I> {

    protected I id;
}
