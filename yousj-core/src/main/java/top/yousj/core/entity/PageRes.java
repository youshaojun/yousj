package top.yousj.core.entity;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRes<T> implements Serializable {

	private static final long serialVersionUID = 7868868017883272038L;

	private final int num;

	private final int size;

	private final long total;

	private final int pages;

	private final boolean hasNext;

	private final List<T> data;

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
