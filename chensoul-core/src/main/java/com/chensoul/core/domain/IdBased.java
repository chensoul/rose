package com.chensoul.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdBased<I extends Serializable> implements HasId<I> {

	protected I id;

}
