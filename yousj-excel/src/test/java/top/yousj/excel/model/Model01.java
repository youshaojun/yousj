package top.yousj.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import top.yousj.excel.utils.ExcelUtil;

/**
 * @author yousj
 * @since 2023-02-07
 */
@Data
public class Model01 {

	@ColumnWidth(30)
	@ExcelProperty(value = "标题")
	private String title = ExcelUtil.Mark.RED.getPre() + "测试一下" + ExcelUtil.Mark.RED.getPost() + "高亮";

	@ColumnWidth(30)
	@ExcelProperty(value = "链接")
	private String url = "https://www.baidu.com";

	@ColumnWidth(30)
	@ExcelProperty(value = "测试01")
	private String test01 = ExcelUtil.Mark.GREEN.getPre() + "测试一下" + ExcelUtil.Mark.GREEN.getPost() + "高亮";

	@ColumnWidth(10)
	@ExcelProperty(value = "测试02")
	private String test02 = ExcelUtil.Mark.YELLOW.getPre() + "测试一下" + ExcelUtil.Mark.YELLOW.getPost() + "高亮";

	@ColumnWidth(30)
	@ExcelProperty(value = "测试03")
	private String test03 = ExcelUtil.Mark.PINK.getPre() + "测试一下" + ExcelUtil.Mark.PINK.getPost() + "高亮";

	@ColumnWidth(60)
	@ExcelProperty(value = "测试04")
	private String test04 = ExcelUtil.Mark.BLUE.getPre() + "测试一下" + ExcelUtil.Mark.BLUE.getPost() + "高亮";

}
