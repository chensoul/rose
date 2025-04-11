package com.chensoul.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 指定数据列合并，只要指定合并列范围的单元格值和前一行的主键值相同，就合并
 */
public class MergeByPrimaryKeyStrategy implements CellWriteHandler {

    /**
     * 合并起始行索引
     */
    private final int headRowNumber;

    /**
     * 合并列的范围索引
     */
    private final int[] mergeColumnIndex;

    private int primaryKeyIndex = 0;

    private boolean mergeNullValue = false;

    public MergeByPrimaryKeyStrategy(int headRowNumber, int[] mergeColumnIndex) {
        this.mergeColumnIndex = mergeColumnIndex;
        this.headRowNumber = headRowNumber;
    }

    public MergeByPrimaryKeyStrategy(
            int headRowNumber, int[] mergeColumnIndex, int primaryKeyIndex, boolean mergeNullValue) {
        this.mergeColumnIndex = mergeColumnIndex;
        this.headRowNumber = headRowNumber;
        this.primaryKeyIndex = primaryKeyIndex;
        this.mergeNullValue = mergeNullValue;
    }

    @Override
    public void beforeCellCreate(
            WriteSheetHolder writeSheetHolder,
            WriteTableHolder writeTableHolder,
            Row row,
            Head head,
            Integer integer,
            Integer integer1,
            Boolean aBoolean) {}

    @Override
    public void afterCellCreate(
            WriteSheetHolder writeSheetHolder,
            WriteTableHolder writeTableHolder,
            Cell cell,
            Head head,
            Integer integer,
            Boolean aBoolean) {
        // 隐藏 primaryKeyIndex 列
        // writeSheetHolder.getSheet().setColumnHidden(primaryKeyIndex, true);
    }

    @Override
    public void afterCellDispose(
            WriteSheetHolder writeSheetHolder,
            WriteTableHolder writeTableHolder,
            List<WriteCellData<?>> list,
            Cell cell,
            Head head,
            Integer integer,
            Boolean aBoolean) {
        // 当前行
        int curRowIndex = cell.getRowIndex();
        // 当前列
        int curColIndex = cell.getColumnIndex();
        if (curRowIndex > headRowNumber) {
            for (int columnIndex : mergeColumnIndex) {
                if (curColIndex == columnIndex) {
                    mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex, primaryKeyIndex);
                    break;
                }
            }
        }
    }

    /**
     * 当前单元格向上合并
     *
     * @param writeSheetHolder
     * @param cell
     * @param curRowIndex
     * @param curColIndex
     */
    private void mergeWithPrevRow(
            WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex, int primaryKeyIndex) {
        // 当前行的第一个Cell
        Cell curFirstCell = cell.getSheet().getRow(curRowIndex).getCell(primaryKeyIndex);
        Object curFirstData = curFirstCell.getCellType() == CellType.STRING
                ? curFirstCell.getStringCellValue()
                : curFirstCell.getNumericCellValue();
        // 上一行的第一个Cell
        Cell preFirstCell = cell.getSheet().getRow(curRowIndex - 1).getCell(primaryKeyIndex);
        Object preFirstData = preFirstCell.getCellType() == CellType.STRING
                ? preFirstCell.getStringCellValue()
                : preFirstCell.getNumericCellValue();

        // 当前行的首列和上一行的首列相同则合并前面（mergeColumnRegion+1）列
        if ((mergeNullValue && curFirstData.equals(preFirstData))
                || (!mergeNullValue
                        && curFirstData != null
                        && preFirstData != null
                        && curFirstData.equals(preFirstData))) {
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergedRegions.size() && !isMerged; i++) {
                CellRangeAddress cellAddresses = mergedRegions.get(i);
                // 若上 一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellAddresses.isInRange(curRowIndex - 1, curColIndex)) {
                    sheet.removeMergedRegion(i);
                    cellAddresses.setLastRow(curRowIndex);
                    sheet.addMergedRegion(cellAddresses);
                    isMerged = true;
                }
            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellAddresses =
                        new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
                sheet.addMergedRegion(cellAddresses);
            }
        }
    }
}
