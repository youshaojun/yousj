package top.yousj.commons.exception;

import top.yousj.commons.entity.R;


/**
 * 异常处理
 *
 * @author yousj
 * @since 2023-01-05
 */
public interface ExceptionAdviceHandler {

	R<String> handle(Exception ex);

}
