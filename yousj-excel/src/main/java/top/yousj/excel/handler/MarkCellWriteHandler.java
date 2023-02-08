package top.yousj.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import top.yousj.excel.utils.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本高亮
 *
 * @author yousj
 * @since 2022-12-29
 */
public class MarkCellWriteHandler implements CellWriteHandler {

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		if (cell.getCellType() == CellType.STRING && StringUtils.isNotBlank(cell.getStringCellValue())) {
			for (ExcelUtil.Mark mark : ExcelUtil.Mark.values()) {
				setCellMark(writeSheetHolder, cell, mark);
			}
		}
	}

	private void setCellMark(WriteSheetHolder writeSheetHolder, Cell cell, ExcelUtil.Mark mark) {
		CreationHelper createHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
		String stringCellValue = cell.getStringCellValue();
		int preMarkLength = mark.getPre().length();
		int postMarkLength = mark.getPost().length();
		int totalMarkLength = preMarkLength + postMarkLength;
		List<ExcelUtil.ExcelIndex> list = new ArrayList<>();
		while (true) {
			int lastEndIndexOf = stringCellValue.lastIndexOf(mark.getPost());
			int lastStartIndexOf = stringCellValue.lastIndexOf(mark.getPre());
			if (lastEndIndexOf < 0 || lastStartIndexOf < 0) {
				break;
			}
			ExcelUtil.ExcelIndex indexBean = new ExcelUtil.ExcelIndex();
			indexBean.setEndIndex(totalMarkLength + lastEndIndexOf);
			indexBean.setStartIndex(lastStartIndexOf);
			stringCellValue = stringCellValue.substring(0, lastStartIndexOf) + stringCellValue.substring(lastStartIndexOf, lastEndIndexOf + postMarkLength).replaceAll(mark.getPre() + "(.*)" + mark.getPost(), "$1") + stringCellValue.substring(lastEndIndexOf + postMarkLength);
			list.add(indexBean);
		}
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				ExcelUtil.ExcelIndex indexBean = list.get(i);
				indexBean.setStartIndex(indexBean.getStartIndex() - totalMarkLength * (list.size() - i - 1));
				indexBean.setEndIndex(indexBean.getEndIndex() - totalMarkLength * (list.size() - i) - preMarkLength);
			}
			RichTextString richTextString = createHelper.createRichTextString(stringCellValue);
			String fontName = "宋体";
			short fontHeightInPoints = (short) 10;
			Font font = writeSheetHolder.getSheet().getWorkbook().createFont();
			font.setColor(mark.getColor());
			font.setFontName(fontName);
			font.setFontHeightInPoints(fontHeightInPoints);
			Font normalFont = writeSheetHolder.getSheet().getWorkbook().createFont();
			normalFont.setFontName(fontName);
			normalFont.setFontHeightInPoints(fontHeightInPoints);
			richTextString.applyFont(normalFont);
			for (ExcelUtil.ExcelIndex indexBean : list) {
				richTextString.applyFont(indexBean.getStartIndex(), indexBean.getEndIndex(), font);
			}
			cell.setCellValue(richTextString);
		}
	}

	@Override
	public int order() {
		return 1000;
	}

}
