package top.yousj.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yousj.excel.utils.ExcelUtil;

/**
 * @author yousj
 * @since 2023-02-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@HeadStyle(fillForegroundColor = 44,
	verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.CENTER, fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(shrinkToFit = BooleanEnum.TRUE, wrapped = BooleanEnum.TRUE,
	verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentFontStyle(fontHeightInPoints = 10)
public class Model04 {

	@ExcelProperty(value = "标题")
	@ColumnWidth(30)
	private String title = ExcelUtil.Mark.RED.getPre() + "测试一下" + ExcelUtil.Mark.RED.getPost() + "高亮";

	@ExcelProperty(value = "链接")
	@ColumnWidth(30)
	private String url = "https://www.baidu.com";

	@ContentStyle(shrinkToFit = BooleanEnum.TRUE, wrapped = BooleanEnum.TRUE,
		verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.LEFT)
	@ColumnWidth(100)
	@ExcelProperty(value = "描述")
	private String desc = "O(∩_∩)O哈哈~O(∩_∩)O哈哈~O(∩_∩)O哈哈~O(∩_∩)O~";

}
