package top.yousj.excel;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.excel.handler.ContentCellStylePostWriteHandler;
import top.yousj.excel.handler.MultiSheetUrlCellWriteHandler;
import top.yousj.excel.handler.UrlCellWriteHandler;
import top.yousj.excel.model.Model01;
import top.yousj.excel.model.Model02;
import top.yousj.excel.model.Model03;
import top.yousj.excel.utils.ExcelUtil;

import java.util.Arrays;
import java.util.Collections;

public class ExcelTests {

	/**
	 * 设置高亮标记
	 */
	@Test
	void mark() {
		ExcelUtil.write(FileTypeEnum.XLSX, "测试一下", Model01.class, Collections.singletonList(new Model01()));
	}

	/**
	 * 设置超链接(列即是url)
	 */
	@Test
	void setUrl01() {
		ExcelUtil.write(FileTypeEnum.XLSX, "测试一下", Model02.class, Arrays.asList(new Model02(), new Model02("测试一下", "")), new UrlCellWriteHandler());
	}

	/**
	 * 设置超链接(列不是url)
	 * {@link ExcelUtil.Hyperlink}
	 */
	@SneakyThrows
	@Test
	void setUrl02() {
		ExcelUtil.write(FileTypeEnum.XLSX, "测试一下", Model03.class,
			Arrays.asList(new Model03(),
				new Model03("测试一下", ExcelUtil.OBJECT_MAPPER.writeValueAsString(new ExcelUtil.Hyperlink("测试一下", ""))),
				new Model03("测试一下", ExcelUtil.OBJECT_MAPPER.writeValueAsString(new ExcelUtil.Hyperlink("测试一下", "https://www.baidu.com")))
			),
			new UrlCellWriteHandler(1, "--", false),
			new ContentCellStylePostWriteHandler()
		);
	}

	/**
	 * 多sheet
	 */
	@Test
	void setMultiSheet() {
		MultiSheetUrlCellWriteHandler multiSheetUrlCellWriteHandler = new MultiSheetUrlCellWriteHandler();
		multiSheetUrlCellWriteHandler.setMultiSheets(Arrays.asList(
			new MultiSheetUrlCellWriteHandler.MultiSheet("工作表01", 1),
			new MultiSheetUrlCellWriteHandler.MultiSheet("工作表02", 1)
		));

		ExcelUtil.write(FileTypeEnum.XLSX, Arrays.asList("工作表01", "工作表02"), Model02.class,
			Arrays.asList(
				Arrays.asList(new Model02(), new Model02("测试一下工作表01", "")),
				Arrays.asList(new Model02(), new Model02("测试一下工作表02", ""))
			),
			multiSheetUrlCellWriteHandler,
			new ContentCellStylePostWriteHandler()
			);
	}

}
