package com.chensoul.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.chensoul.excel.ReadFailMessageAware;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ReadValidateListener<T extends ReadFailMessageAware> extends AnalysisEventListener<T> {

	public static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Getter
	private final List<T> failData = new ArrayList<>();

	@Getter
	private final List<T> successData = new ArrayList<>();

	private final BiConsumer<List<T>, List<T>> consumer;

	public ReadValidateListener(BiConsumer<List<T>, List<T>> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		validateData(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		if (consumer != null) {
			consumer.accept(successData, failData);
		}
	}

	protected void validateData(T data) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(data, Default.class);
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			data.setMessage(constraintViolations.stream().map(a -> a.getMessage()).collect(Collectors.joining("、")));
			failData.add(data);
		} else {
			String customValidateResult = customValidate(successData, data);
			if (StringUtils.isEmpty(customValidateResult)) {
				successData.add(data);
			} else {
				data.setMessage(customValidateResult);
				failData.add(data);
			}
		}
	}

	protected String customValidate(List<T> successData, T data) {
		return null;
	}

}
