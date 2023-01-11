package top.yousj.core.exception;

import top.yousj.core.entity.R;


/**
 * 异常处理
 *
 * @author yousj
 * @since 2023-01-05
 */
public interface ExceptionAdviceHandler {

	R<String> handle(Exception ex);

}
