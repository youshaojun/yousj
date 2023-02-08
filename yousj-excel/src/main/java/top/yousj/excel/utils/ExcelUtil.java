package top.yousj.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.commons.utils.ExportUtil;
import top.yousj.excel.handler.MarkCellWriteHandler;
import top.yousj.excel.model.ExcelData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author yousj
 * @since 2022-12-29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static File write(FileTypeEnum fileTypeEnum, ExcelData data, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, Collections.singletonList(data), StyleUtil.defaultStyle(), null, cellWriteHandlers);
	}

	public static File write(FileTypeEnum fileTypeEnum, List<ExcelData> dataList, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, dataList, StyleUtil.defaultStyle(), null, cellWriteHandlers);
	}

	public static File writeWithAnnotation(FileTypeEnum fileTypeEnum, ExcelData data, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, Collections.singletonList(data), null, null, cellWriteHandlers);
	}

	public static File writeWithSimpleColumnWidth(FileTypeEnum fileTypeEnum, ExcelData data, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, Collections.singletonList(data), StyleUtil.defaultStyle(), new SimpleColumnWidthStyleStrategy(30), cellWriteHandlers);
	}

	public static File writeWithSimpleColumnWidth(FileTypeEnum fileTypeEnum, List<ExcelData> dataList, CellWriteHandler... cellWriteHandlers) {
		return write(fileTypeEnum, dataList, StyleUtil.defaultStyle(), new SimpleColumnWidthStyleStrategy(30), cellWriteHandlers);
	}

	@SneakyThrows
	public static File write(FileTypeEnum fileTypeEnum, List<ExcelData> dataList, AbstractCellStyleStrategy defaultStyle,
							 SimpleColumnWidthStyleStrategy simpleColumnWidthStyleStrategy, CellWriteHandler... cellWriteHandlers) {
		File file = ExportUtil.newFile(fileTypeEnum);
		try (FileOutputStream out = new FileOutputStream(file)) {
			ExcelWriter writer = getExcelWriter(out, defaultStyle, simpleColumnWidthStyleStrategy, cellWriteHandlers);
			for (int i = 0; i < dataList.size(); i++) {
				ExcelData excelData = dataList.get(i);
				WriteSheet writeSheet = new WriteSheet();
				writeSheet.setClazz(excelData.getClazz());
				writeSheet.setAutoTrim(true);
				writeSheet.setSheetNo(i + 1);
				writeSheet.setSheetName(excelData.getSheetName());
				writer.write(excelData.getDataList(), writeSheet);
			}
			writer.finish();
		}
		return file;
	}

	public static ExcelWriter getExcelWriter(FileOutputStream out, AbstractCellStyleStrategy defaultStyle,
											 SimpleColumnWidthStyleStrategy simpleColumnWidthStyleStrategy, CellWriteHandler... cellWriteHandlers) {
		ExcelWriterBuilder writerBuilder = EasyExcel.write(out)
			.inMemory(true)
			.needHead(true)
			.autoTrim(true)
			.registerWriteHandler(new MarkCellWriteHandler());
		if (Objects.nonNull(defaultStyle)) {
			writerBuilder.registerWriteHandler(defaultStyle);
		}
		if (Objects.nonNull(simpleColumnWidthStyleStrategy)) {
			writerBuilder.registerWriteHandler(simpleColumnWidthStyleStrategy);
		}
		Arrays.stream(cellWriteHandlers).forEach(writerBuilder::registerWriteHandler);
		return writerBuilder.build();
	}

	public static String mark(String source, Mark mark) {
		if (StringUtils.isBlank(source)) {
			return source;
		}
		return mark.getPre() + source + mark.getPost();
	}


	@Getter
	@AllArgsConstructor
	public enum Mark {
		/**
		 * <a href="https://www.cnblogs.com/xy2401/p/3295965.html"/>
		 */
		RED("<red>", "</red>", Font.COLOR_RED, "红色"),
		GREEN("<green>", "</green>", (short) 11, "绿色"),
		BLUE("<blue>", "</blue>", (short) 12, "蓝色"),
		YELLOW("<yellow>", "</yellow>", (short) 13, "黄色"),
		PINK("<pink>", "</pink>", (short) 14, "粉色"),
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
