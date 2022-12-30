package top.yousj.excel.strategy;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;
import top.yousj.excel.handler.ContentCellStylePostWriteHandler;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class DefaultCellStyleStrategy {

	private static AbstractCellStyleStrategy get() {
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
		WriteCellStyle contentWriteCellStyle = ContentCellStylePostWriteHandler.getWriteCellStyle();
		contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
		contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
		contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
		contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
		return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
	}

}
