package com.chensoul.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {
	String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Class<LocalDateTime> supportJavaTypeKey() {
		return LocalDateTime.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
										   GlobalConfiguration globalConfiguration) {
		return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN));
	}

	@Override
	public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {
		return new WriteCellData(value.format(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
	}
}
