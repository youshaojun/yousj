package top.yousj.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import top.yousj.excel.utils.StyleUtil;

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

	private static CellStyle defaultCellStyle = null;
	private static CellStyle hyperlinkCellStyle = null;

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		if (!isHead && cell.getCellType() == CellType.STRING && ("-".equals(cell.getStringCellValue()) || "--".equals(cell.getStringCellValue()))) {
			boolean isHyperlink = Objects.nonNull(cell.getHyperlink());
			initCellStyle(writeSheetHolder, isHyperlink);
			cell.setCellStyle(isHyperlink ? hyperlinkCellStyle : defaultCellStyle);
		}
	}

	private void initCellStyle(WriteSheetHolder writeSheetHolder, boolean isHyperlink) {
		if (isHyperlink && Objects.isNull(hyperlinkCellStyle)) {
			hyperlinkCellStyle = StyleUtil.buildContentCellStyle(writeSheetHolder, true);
			return;
		}
		if (!isHyperlink && Objects.isNull(defaultCellStyle)) {
			hyperlinkCellStyle = StyleUtil.buildContentCellStyle(writeSheetHolder, false);
		}
	}



}
