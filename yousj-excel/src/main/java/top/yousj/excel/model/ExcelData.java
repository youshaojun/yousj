package top.yousj.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yousj
 * @since 2023-02-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelData {

	private String sheetName;

	private Class<?> clazz;

	private List<?> dataList;

}
