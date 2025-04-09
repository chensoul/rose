package com.chensoul.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.CellExtra;
import com.chensoul.excel.ExcelMergeHelper;
import com.chensoul.excel.ReadFailMessageAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class ReadMergeValidateListener<T extends ReadFailMessageAware> extends ReadValidateListener<T> {

	private final List<T> datas;

	private final List<CellExtra> extraList;

	private final Integer headRowNumber;

	private final int[] mergeColumnIndex;

	public ReadMergeValidateListener(Integer headRowNumber, int[] mergeColumnIndex,
			BiConsumer<List<T>, List<T>> consumer) {
		super(consumer);

		this.headRowNumber = headRowNumber;
		this.mergeColumnIndex = mergeColumnIndex;
		this.datas = new ArrayList<>();
		this.extraList = new ArrayList<>();
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		context.readWorkbookHolder().setIgnoreEmptyRow(false);
		this.datas.add(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		for (T data : getData()) {
			validateData(data);
		}
		super.doAfterAllAnalysed(context);
	}

	@Override
	public void extra(CellExtra extra, AnalysisContext context) {
		if (Objects.requireNonNull(extra.getType()) == CellExtraTypeEnum.MERGE) {
			if (extra.getRowIndex() >= headRowNumber) {
				extraList.add(extra);
			}
		}
	}

	private List<T> getData() {
		if (CollectionUtils.isEmpty(extraList)) {
			return datas;
		}
		checkMergeIndex();

		return new ExcelMergeHelper().explainMergeData(datas, extraList, headRowNumber);
	}

	private void checkMergeIndex() {
		if (extraList.size() % mergeColumnIndex.length != 0) {
			throw new RuntimeException("单元格合并错误，请检查后重新上传");
		}
	}

}
