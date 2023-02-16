package top.yousj.commons.exception;

import org.springframework.core.Ordered;
import top.yousj.commons.entity.R;

/**
 * 异常处理
 *
 * @author yousj
 * @since 2023-01-05
 */
public interface ExceptionAdviceHandler extends Ordered {

	R<String> handle(Exception ex);

	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
