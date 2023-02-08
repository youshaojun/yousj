package top.yousj.excel.utils;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

/**
 * @author yousj
 * @since 2023-01-17
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StyleUtil {

	public static AbstractCellStyleStrategy defaultStyle() {
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
		headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		WriteFont headWriteFont = new WriteFont();
		headWriteFont.setFontName("宋体");
		headWriteFont.setFontHeightInPoints((short) 10);
		headWriteFont.setBold(true);
		headWriteCellStyle.setWriteFont(headWriteFont);
		headWriteCellStyle.setBorderTop(BorderStyle.THIN);
		headWriteCellStyle.setBorderRight(BorderStyle.THIN);
		headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
		headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
		WriteCellStyle contentWriteCellStyle = StyleUtil.getWriteCellStyle();
		contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
		contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
		contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
		contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
		return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
	}

	/**
	 * 请勿在循环中构建CellStyle, 尽量复用
	 * maxCellStyles 64000 {@link org.apache.poi.xssf.model.StylesTable#MAXIMUM_STYLE_ID}
	 */
	public static CellStyle buildContentCellStyle(WriteSheetHolder writeSheetHolder) {
		return buildContentCellStyle(writeSheetHolder, false);
	}

	public static CellStyle buildContentCellStyle(WriteSheetHolder writeSheetHolder, boolean isHyperlink) {
		WriteCellStyle contentWriteCellStyle = getWriteCellStyle();
		contentWriteCellStyle.setWriteFont(getWriteFont(isHyperlink));
		return com.alibaba.excel.util.StyleUtil.buildCellStyle(writeSheetHolder.getSheet().getWorkbook(), null, contentWriteCellStyle);
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

}
