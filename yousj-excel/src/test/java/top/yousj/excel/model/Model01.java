package top.yousj.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import top.yousj.excel.utils.ExcelUtil;

/**
 * @author yousj
 * @since 2023-02-07
 */
@Data
public class Model01 {

	@ExcelProperty(value = "标题")
	private String title = ExcelUtil.Mark.RED.getPre() + "测试一下" + ExcelUtil.Mark.RED.getPost() + "高亮";

	@ExcelProperty(value = "链接")
	private String url = "https://www.baidu.com";

}
