package top.yousj.datasource.entity;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRes<T> implements Serializable {

	private static final long serialVersionUID = 7868868017883272038L;

	private int num;

	private int size;

	private long total;

	private int pages;

	private boolean hasNext;

	private List<T> data;

	public static <T> PageRes<T> of(PageInfo<T> info) {
		return new PageRes<>(info.getPageNum(), info.getPageSize(), info.getTotal(), info.getPages(), info.isHasNextPage(), info.getList());
	}

	public static <T> PageRes<T> of(PageInfo<?> info, List<T> result) {
		return new PageRes<>(info.getPageNum(), info.getPageSize(), info.getTotal(), info.getPages(),
			info.isHasNextPage(), result);
	}

	public static <T> PageRes<T> empty(Page page) {
		return new PageRes<>(page.getPageNum(), page.getPageSize(), 0, 0, false, Collections.emptyList());
	}

}
