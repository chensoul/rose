package com.chensoul.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @param <I>
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ContactBased<I extends Serializable> extends BaseDataWithExtra<I> implements HasEmail, HasId<I> {

	private static final long serialVersionUID = 5047448057830660988L;

	protected String phone;

	@Pattern(regexp = EMAIL_REGEXP, message = "邮箱不合法")
	protected String email;

	protected String country;

	protected String state;

	protected String city;

	protected String address;

	protected String zip;

}
