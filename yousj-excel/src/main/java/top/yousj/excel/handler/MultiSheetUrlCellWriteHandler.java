package top.yousj.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import top.yousj.commons.utils.FuncUtil;

import java.util.List;
import java.util.Objects;

/**
 * 多sheet列表设置源链接可点击
 *
 * @author yousj
 * @since 2022-12-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiSheetUrlCellWriteHandler implements CellWriteHandler {

    private List<MultiSheet> multiSheets;

    private String ignoreStr = "--";

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        multiSheets.stream()
            .filter(e -> (StringUtils.equals(writeSheetHolder.getSheetName(), e.getSheetName())))
            .findFirst()
            .ifPresent(e -> FuncUtil.run(!isHead && cell.getColumnIndex() == e.getColumnIndex() && !Objects.equals(cell.getStringCellValue(), ignoreStr), () -> UrlCellWriteHandler.setHyperlink(writeSheetHolder, cell, ignoreStr)));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultiSheet {

        private String sheetName;

        private int columnIndex = 1;

    }

}
