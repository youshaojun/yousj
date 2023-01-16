package top.yousj.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Objects;

/**
 * 内容样式后置处理
 * 设置默认填充空值的样式
 *
 * @author yousj
 * @since 2022-12-30
 */
public class ContentCellStylePostWriteHandler implements CellWriteHandler {

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		if (!isHead && cell.getCellType() == CellType.STRING && ("-".equals(cell.getStringCellValue()) || "--".equals(cell.getStringCellValue()))) {
			cell.setCellStyle(buildContentCellStyle(writeSheetHolder, Objects.nonNull(cell.getHyperlink())));
		}
	}

	/**
	 * maxCellStyles 64000 {@link org.apache.poi.xssf.model.StylesTable#MAXIMUM_STYLE_ID}
	 */
	public static CellStyle buildContentCellStyle(WriteSheetHolder writeSheetHolder) {
		return buildContentCellStyle(writeSheetHolder, false);
	}

	public static CellStyle buildContentCellStyle(WriteSheetHolder writeSheetHolder, boolean isHyperlink) {
		WriteCellStyle contentWriteCellStyle = getWriteCellStyle();
		contentWriteCellStyle.setWriteFont(getWriteFont(isHyperlink));
		return buildContentCellStyle(writeSheetHolder.getSheet().getWorkbook(), contentWriteCellStyle);
	}

	public static WriteCellStyle getWriteCellStyle() {
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		contentWriteCellStyle.setShrinkToFit(true);
		contentWriteCellStyle.setWrapped(true);
		contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		return contentWriteCellStyle;
	}

	private static WriteFont getWriteFont(boolean isHyperlink) {
		WriteFont contentWriteFont = new WriteFont();
		contentWriteFont.setFontName("宋体");
		contentWriteFont.setFontHeightInPoints((short) 10);
		// 超链接的"-"、"--"也设置成蓝色
		if (isHyperlink) {
			contentWriteFont.setColor((short) 12);
		}
		return contentWriteFont;
	}

	public static CellStyle buildContentCellStyle(Workbook workbook, WriteCellStyle writeCellStyle) {
		CellStyle cellStyle = workbook.createCellStyle();
		if (writeCellStyle == null) {
			return cellStyle;
		}
		StyleUtil.buildCellStyle(workbook, cellStyle, writeCellStyle);
		return cellStyle;
	}

}
