package com.chensoul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.CellExtra;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class ExcelMergeHelper<T> {

	/**
	 * 处理合并单元格
	 *
	 * @param data               解析数据
	 * @param extraMergeInfoList 合并单元格信息
	 * @param headRowNumber      起始行
	 * @return 填充好的解析数据
	 */
	public List<T> explainMergeData(List<T> data, List<CellExtra> extraMergeInfoList, Integer headRowNumber) {
		// 循环所有合并单元格信息
		extraMergeInfoList.forEach(cellExtra -> {
			int firstRowIndex = cellExtra.getFirstRowIndex() - headRowNumber;
			int lastRowIndex = cellExtra.getLastRowIndex() - headRowNumber;
			int firstColumnIndex = cellExtra.getFirstColumnIndex();
			int lastColumnIndex = cellExtra.getLastColumnIndex();
			// 获取初始值
			Object initValue = getInitValueFromList(firstRowIndex, firstColumnIndex, data);
			// 设置值
			for (int i = firstRowIndex; i <= lastRowIndex; i++) {
				for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
					setInitValueToList(initValue, i, j, data);
				}
			}
		});
		return data;
	}

	/**
	 * 设置合并单元格的值
	 *
	 * @param filedValue  值
	 * @param rowIndex    行
	 * @param columnIndex 列
	 * @param data        解析数据
	 */
	public void setInitValueToList(Object filedValue, Integer rowIndex, Integer columnIndex, List<T> data) {
		T object = data.get(rowIndex);

		List<Field> fieldList = new ArrayList<>();
		Class clazz = object.getClass();
		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		for (Field field : fieldList) {
			// 提升反射性能，关闭安全检查
			field.setAccessible(true);
			ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
			if (annotation != null) {
				if (annotation.index() == columnIndex) {
					try {
						field.set(object, filedValue);
						break;
					} catch (IllegalAccessException e) {
						log.error("设置合并单元格的值异常：" + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 获取合并单元格的初始值 rowIndex对应list的索引 columnIndex对应实体内的字段
	 *
	 * @param firstRowIndex    起始行
	 * @param firstColumnIndex 起始列
	 * @param data             列数据
	 * @return 初始值
	 */
	private Object getInitValueFromList(Integer firstRowIndex, Integer firstColumnIndex, List<T> data) {
		Object filedValue = null;
		T object = data.get(firstRowIndex);
		List<Field> fieldList = new ArrayList<>();
		Class<? extends Object> clazz = object.getClass();
		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		for (Field field : fieldList) {
			// 提升反射性能，关闭安全检查
			field.setAccessible(true);
			ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
			if (annotation != null) {
				if (annotation.index() == firstColumnIndex) {
					try {
						filedValue = field.get(object);
						break;
					} catch (IllegalAccessException e) {
						log.error("设置合并单元格的初始值异常：" + e.getMessage());
					}
				}
			}
		}
		return filedValue;
	}

}
