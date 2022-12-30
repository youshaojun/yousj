package top.yousj.excel.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Font;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class ExportExcelUtil {

	@Getter
	@AllArgsConstructor
	public enum Mark {

		/**
		 *
		 */
		RED("<red>", "</red>", Font.COLOR_RED, "红色");

		private final String pre;

		private final String post;

		private final short color;

		private final String desc;

	}

	@Data
	public static class ExcelIndex {

		private int startIndex;

		private int endIndex;

	}

	@Data
	@AllArgsConstructor
	public class Hyperlink {

		private String cellValue;

		private String url;

	}

}
