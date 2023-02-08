package top.yousj.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.poi.ss.usermodel.Font;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.commons.utils.ExportUtil;
import top.yousj.excel.handler.MarkCellWriteHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yousj
 * @since 2022-12-29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static File write(FileTypeEnum fileTypeEnum, String sheetName, Class<?> clazz, List<?> data, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, Collections.singletonList(sheetName), clazz, Collections.singletonList(data), cellWriteHandlers);
	}

	@SneakyThrows
	public static File write(FileTypeEnum fileTypeEnum, List<String> sheetNames, Class<?> clazz, List<List<?>> dataList, CellWriteHandler... cellWriteHandlers) {
		File file = ExportUtil.newFile(fileTypeEnum);
		try (FileOutputStream out = new FileOutputStream(file)) {
			ExcelWriterBuilder writerBuilder = EasyExcel.write(out, clazz)
				.inMemory(true)
				.needHead(true)
				.autoTrim(true)
				.registerWriteHandler(StyleUtil.defaultStyle())
				.registerWriteHandler(new SimpleColumnWidthStyleStrategy(30))
				.registerWriteHandler(new MarkCellWriteHandler());
			Arrays.stream(cellWriteHandlers).forEach(writerBuilder::registerWriteHandler);
			ExcelWriter writer = writerBuilder.build();
			for (int i = 0; i < dataList.size(); i++) {
				WriteSheet writeSheet = new WriteSheet();
				writeSheet.setClazz(clazz);
				writeSheet.setAutoTrim(true);
				writeSheet.setSheetNo(i + 1);
				writeSheet.setSheetName(sheetNames.get(i));
				writer.write(dataList.get(i), writeSheet);
			}
			writer.finish();
		}
		return file;
	}

	public static String mark(String source, Mark mark) {
		return mark.getPre() + source + mark.getPost();
	}


	@Getter
	@AllArgsConstructor
	public enum Mark {

		/**
		 *
		 */
		RED("<red>", "</red>", Font.COLOR_RED, "红色"),
		BLUE("<blue>", "</blue>", (short) 12, "蓝色"),
		BLACK("<black>", "</black>", Font.COLOR_NORMAL, "黑色"),

		;

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
	@NoArgsConstructor
	public static class Hyperlink {

		private String cellValue;

		private String url;

	}

}
