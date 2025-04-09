package com.chensoul.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.chensoul.excel.converter.LocalDateTimeConverter;
import com.chensoul.excel.handler.CustomSheetWriteHandler;
import com.chensoul.excel.listener.ReadMergeValidateListener;
import com.chensoul.excel.listener.ReadValidateListener;
import com.chensoul.excel.strategy.MergeByPrimaryKeyStrategy;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ExcelHelper {

	public static final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

	public static void writeExcel(HttpServletResponse response, String filename, Class head, Collection data) {
		writeExcel(response, filename, head, data, (SheetWriteHandler) null, null);
	}

	public static void writeExcel(HttpServletResponse response, String filename, Class head, Collection data,
			MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		writeExcel(response, filename, head, data, (SheetWriteHandler) null, mergeByPrimaryKeyStrategy);
	}

	public static void writeExcel(HttpServletResponse response, String filename, Class head, Collection data,
			Map<Integer, List<String>> explicitListConstraintMap, MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		CustomSheetWriteHandler customSheetWriteHandler = new CustomSheetWriteHandler(explicitListConstraintMap,
				data.size());
		writeExcel(response, filename, head, data, customSheetWriteHandler, mergeByPrimaryKeyStrategy);
	}

	public static void writeExcel(HttpServletResponse response, String filename, Class head, Collection data,
			Map<Integer, List<String>> explicitListConstraintMap) {
		writeExcel(response, filename, head, data, explicitListConstraintMap, null);
	}

	public static void writeExcel(HttpServletResponse response, String filename, Class head, Collection data,
			SheetWriteHandler sheetWriteHandler, MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
			ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), head)
				.registerConverter(localDateTimeConverter);

			// 自定义下拉列表
			if (sheetWriteHandler != null) {
				excelWriterBuilder.registerWriteHandler(sheetWriteHandler);
			}

			// 自定义合并单元格
			if (mergeByPrimaryKeyStrategy != null) {
				excelWriterBuilder.registerWriteHandler(mergeByPrimaryKeyStrategy);
			}

			// 自适应列宽
			// excelWriterBuilder.registerWriteHandler(new
			// AutomaticColumnWidthStrategy());

			// 头和内容策略
			excelWriterBuilder.registerWriteHandler(customHorizontalCellStyleStrategy());

			excelWriterBuilder.autoCloseStream(Boolean.FALSE).sheet(filename).doWrite(data);
		}
		catch (Exception e) {
			throw new RuntimeException("导出文件失败", e);
		}
	}

	public static File writeExcelToFile(String fileName, String sheetName, Class<?> clazz, Collection<?> data) {
		return writeExcelToFile(fileName, sheetName, clazz, data, null, null);
	}

	public static File writeExcelToFile(String fileName, String sheetName, Class<?> clazz, Collection<?> data,
			MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		return writeExcelToFile(fileName, sheetName, clazz, data, null, mergeByPrimaryKeyStrategy);
	}

	public static File writeExcelToFile(String fileName, String sheetName, Class<?> clazz, Collection<?> data,
			Map<Integer, List<String>> explicitListConstraintMap) {
		return writeExcelToFile(fileName, sheetName, clazz, data, explicitListConstraintMap, null);
	}

	public static File writeExcelToFile(String fileName, String sheetName, Class<?> clazz, Collection<?> data,
			Map<Integer, List<String>> explicitListConstraintMap, MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		if (CollectionUtils.isEmpty(data)) {
			return null;
		}
		CustomSheetWriteHandler customSheetWriteHandler = null;
		if (MapUtils.isEmpty(explicitListConstraintMap)) {
			customSheetWriteHandler = new CustomSheetWriteHandler(explicitListConstraintMap, data.size());
		}
		String newFilename = String.format("导入失败_%s", fileName);

		File tempFile = null;
		try {
			File tmpDir = new File(System.getProperty("java.io.tmpdir"));
			tempFile = new File(tmpDir, newFilename);
			tempFile.deleteOnExit();

			@Cleanup
			OutputStream outputStream = new FileOutputStream(tempFile);

			ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream, clazz)
				.registerConverter(localDateTimeConverter);

			if (mergeByPrimaryKeyStrategy != null) {
				excelWriterBuilder.registerWriteHandler(mergeByPrimaryKeyStrategy);
			}

			if (customSheetWriteHandler != null) {
				excelWriterBuilder.registerWriteHandler(customSheetWriteHandler);
			}
			// 自适应列宽
			// excelWriterBuilder.registerWriteHandler(new
			// AutomaticColumnWidthStrategy());

			excelWriterBuilder.registerWriteHandler(customHorizontalCellStyleStrategy());

			excelWriterBuilder.autoCloseStream(Boolean.FALSE).sheet(sheetName).doWrite(data);
		}
		catch (Exception e) {
			throw new RuntimeException("导出文件失败", e);
		}
		return tempFile;
	}

	private static HorizontalCellStyleStrategy customHorizontalCellStyleStrategy() {
		// 头的策略
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		// WriteFont headWriteFont = new WriteFont();
		// headWriteFont.setFontHeightInPoints((short) 15);
		// headWriteCellStyle.setWriteFont(headWriteFont);
		// 内容的策略
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		// 内容水平居中
		contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		// 设置垂直居中
		contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// WriteFont contentWriteFont = new WriteFont();
		// // 字体大小
		// contentWriteFont.setFontHeightInPoints((short) 12);
		// contentWriteCellStyle.setWriteFont(contentWriteFont);
		// 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
		return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
	}

	@SneakyThrows
	public static File importAndSaveFail(InputStream inputStream, String filename, String sheetName, Class<?> clazz,
			ReadValidateListener readValidateListener) {
		EasyExcel.read(inputStream, clazz, readValidateListener).sheet().doRead();

		return writeExcelToFile(filename, sheetName, clazz, readValidateListener.getFailData());
	}

	@SneakyThrows
	public static File mergeImportAndSaveFail(InputStream inputStream, String filename, String sheetName,
			Integer headRowNumber, Class<?> clazz, ReadMergeValidateListener readValidateListener,
			MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		return mergeImportAndSaveFail(inputStream, sheetName, filename, 0, headRowNumber, clazz, readValidateListener,
				mergeByPrimaryKeyStrategy);
	}

	@SneakyThrows
	public static File mergeImportAndSaveFail(InputStream inputStream, String filename, String sheetName,
			Integer sheetNo, Integer headRowNumber, Class<?> clazz, ReadMergeValidateListener readValidateListener,
			MergeByPrimaryKeyStrategy mergeByPrimaryKeyStrategy) {
		EasyExcel.read(inputStream, clazz, readValidateListener)
			.extraRead(CellExtraTypeEnum.MERGE)
			.sheet(sheetNo)
			.headRowNumber(headRowNumber)
			.doRead();

		return writeExcelToFile(filename, sheetName, clazz, readValidateListener.getFailData(),
				mergeByPrimaryKeyStrategy);
	}

}
