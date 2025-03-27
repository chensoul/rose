package com.chensoul.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class CustomSheetWriteHandler implements SheetWriteHandler {
	Map<Integer, List<String>> explicitListConstraintMap;
	Integer rowCount;

	@Override
	public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

	}

	@Override
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
		if (MapUtils.isEmpty(explicitListConstraintMap)) {
			return;
		}
		if (rowCount == null || rowCount < 1) {
			rowCount = 1;
		}

		DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
		int index = 0;

		for (Map.Entry<Integer, List<String>> entry : explicitListConstraintMap.entrySet()) {
			Integer k = entry.getKey();
			List<String> v = entry.getValue();
			Workbook workbook = writeWorkbookHolder.getWorkbook();
			String sheetName = "sheet" + k;
			Sheet provideSheet = workbook.createSheet(sheetName);
			// 从第二个工作簿开始隐藏
			index++;
			// 设置隐藏
			workbook.setSheetHidden(index, true);
			// 2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
			for (int i = 0, length = v.size(); i < length; i++) {
				// i:表示你开始的行数 0表示你开始的列数
				provideSheet.createRow(i).createCell(0).setCellValue(v.get(i));
			}
			Name name = workbook.createName();
			name.setNameName(sheetName);
			// 4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
			name.setRefersToFormula(sheetName + "!$A$1:$A$" + (v.size()));

			CellRangeAddressList addressList = new CellRangeAddressList(1, rowCount, k, k);
			DataValidationConstraint constraint = helper.createFormulaListConstraint(sheetName);
			DataValidation dataValidation = helper.createValidation(constraint, addressList);
			dataValidation.setSuppressDropDownArrow(true);
			writeSheetHolder.getSheet().addValidationData(dataValidation);
		}
	}
}
