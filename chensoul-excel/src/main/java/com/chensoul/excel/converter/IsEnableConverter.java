package com.chensoul.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class IsEnableConverter implements Converter<Boolean> {

	@Override
	public Class<Boolean> supportJavaTypeKey() {
		return Boolean.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty excelContentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		return !StringUtils.isBlank(cellData.getStringValue()) && !cellData.getStringValue().equals("禁用");
	}

	@Override
	public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty excelContentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		return new WriteCellData<>(value ? "启用" : "禁用");
	}

}
