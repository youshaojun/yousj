package top.yousj.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import top.yousj.excel.utils.ExcelUtil;

import java.util.List;
import java.util.Objects;

/**
 * 设置源链接可点击
 *
 * @author yousj
 * @since 2022-12-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlCellWriteHandler implements CellWriteHandler {

	/**
	 * 指定url列
	 */
	private int columnIndex = 1;

	/**
	 * 忽略字符
	 */
	private String ignoreStr = "--";

	/**
	 * 默认使用当前url值作为CellValue
	 * {@code false} {@link ExcelUtil.Hyperlink}
	 */
	private boolean useDefaultCellValue = true;

	public UrlCellWriteHandler(int columnIndex, String ignoreStr) {
		this.columnIndex = columnIndex;
		this.ignoreStr = ignoreStr;
	}

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		if (!isHead && cell.getColumnIndex() == columnIndex && !Objects.equals(cell.getStringCellValue(), ignoreStr)) {
			setHyperlink(writeSheetHolder, cell, useDefaultCellValue, ignoreStr);
		}
	}

	public static void setHyperlink(WriteSheetHolder writeSheetHolder, Cell cell, String ignoreStr) {
		setHyperlink(writeSheetHolder, cell, true, ignoreStr);
	}

	public static void setHyperlink(WriteSheetHolder writeSheetHolder, Cell cell, boolean useDefaultCellValue, String ignoreStr) {
		CreationHelper helper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
		String cellValue = cell.getStringCellValue();
		if (StringUtils.isBlank(cellValue) || cellValue.equals(ignoreStr)) {
			return;
		}
		// 列的值就是url
		if (useDefaultCellValue) {
			Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress(cellValue);
			cell.setHyperlink(hyperlink);
			return;
		}
		// 列的值不是url
		try {
			ExcelUtil.Hyperlink hyperlinkProperties = ExcelUtil.OBJECT_MAPPER.readValue(cellValue, ExcelUtil.Hyperlink.class);
			cell.setCellValue(hyperlinkProperties.getCellValue());
			String url = hyperlinkProperties.getUrl();
			if (StringUtils.isBlank(url) || url.equals(ignoreStr)) {
				return;
			}
			Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress(hyperlinkProperties.getUrl());
			cell.setHyperlink(hyperlink);
		} catch (Exception e) {
			cell.setCellValue(ignoreStr);
		}
	}

}
