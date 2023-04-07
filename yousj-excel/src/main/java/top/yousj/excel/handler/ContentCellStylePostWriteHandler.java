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
import org.apache.poi.ss.usermodel.*;
import top.yousj.commons.utils.FuncUtil;
import top.yousj.excel.utils.ExcelUtil;

import java.util.List;
import java.util.Objects;

/**
 * 内容样式后置处理
 *
 * @author yousj
 * @since 2022-12-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCellStylePostWriteHandler implements CellWriteHandler {

    private String ignoreStr = "--";

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (Objects.nonNull(cell.getHyperlink())) {
            cell.setCellValue(ExcelUtil.mark(StringUtils.defaultIfBlank(cell.getStringCellValue(), ignoreStr), ExcelUtil.Mark.BLUE));
            return;
        }
        FuncUtil.run(!isHead && cell.getCellType() == CellType.STRING && (StringUtils.isBlank(cell.getStringCellValue()) || ignoreStr.equals(cell.getStringCellValue())), () -> cell.setCellValue(ExcelUtil.mark(ignoreStr, ExcelUtil.Mark.BLACK)));
    }

    @Override
    public int order() {
        return 999;
    }

}
