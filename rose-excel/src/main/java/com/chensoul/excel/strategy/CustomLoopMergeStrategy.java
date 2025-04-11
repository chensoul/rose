package com.chensoul.excel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class CustomLoopMergeStrategy extends AbstractMergeStrategy {

    private final LinkedList<Integer> eachRow;

    private final int columnIndex;

    private final int maxColumn;

    private final LinkedList<Integer> totalRows;

    public CustomLoopMergeStrategy(List<Integer> eachRow, int columnIndex, int maxColumn) {
        if (eachRow.stream().anyMatch(row -> row < 1)) {
            throw new IllegalArgumentException("EachRows must be greater than 1");
        }
        this.eachRow = new LinkedList<>(eachRow);
        this.columnIndex = columnIndex;
        this.maxColumn = maxColumn;
        final int[] acc = {0};
        this.totalRows = eachRow.stream()
                .map(item -> {
                    int result = item + acc[0], item1 = item + acc[0];

                    acc[0] = item1;
                    if (item == 1) {
                        result = 0;
                    }

                    return result;
                })
                .filter(i -> i != 0)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (totalRows.isEmpty() || eachRow.isEmpty() || head.getColumnIndex() > maxColumn) {
            return;
        }

        if (head.getColumnIndex() == maxColumn && eachRow.getFirst() == 1) {
            eachRow.removeFirst();
            return;
        }

        if (relativeRowIndex >= totalRows.getFirst()) {
            totalRows.removeFirst();
            eachRow.removeFirst();
        }

        if (head.getColumnIndex() == columnIndex
                && !totalRows.isEmpty()
                && !eachRow.isEmpty()
                && relativeRowIndex == totalRows.getFirst() - eachRow.getFirst()) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(
                    cell.getRowIndex(),
                    cell.getRowIndex() + eachRow.getFirst() - 1,
                    cell.getColumnIndex(),
                    cell.getColumnIndex());
            sheet.addMergedRegion(cellRangeAddress);
        }
    }
}
